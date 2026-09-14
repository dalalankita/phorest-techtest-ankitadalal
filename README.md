# Fruit Machine

A virtual fruit machine exposed as a REST API. You configure a machine — how many slots, how many
colours, and the payout rules — then spin it: each play fills the slots with random colours,
works out the prize, and settles the machine's money. Built with Spring Boot, with the game logic
kept as a plain-Java core that has no knowledge of the web.

## Requirements

- JDK 21+
- Maven (or run it in Docker — see below)

## Running it

```bash
# run locally (starts on http://localhost:8080)
mvn spring-boot:run

# run the tests
mvn test

# run in Docker
docker compose up --build
# or:
docker build -t fruit-machine .
docker run -p 8080:8080 fruit-machine
```

## API

Base path: `/fruit-machine`.

| Method | Path                  | Purpose                                    | Success |
|--------|-----------------------|--------------------------------------------|---------|
| POST   | `/fruit-machine`      | Configure (or reconfigure) the machine     | 201     |
| GET    | `/fruit-machine`      | Read current state (config, float, plays)  | 200     |
| POST   | `/fruit-machine/play` | Spin, evaluate, settle, return the outcome | 200     |

### Configure — `POST /fruit-machine`

Request:

```json
{ "slotCount": 4, "colourCount": 4, "k": 2, "playCost": 1, "startingFloat": 100 }
```

Response (201):

```json
{
  "config": { "slotCount": 4, "colourCount": 4, "k": 2, "playCost": 1, "startingFloat": 100 },
  "currentFloat": 100,
  "freePlays": 0
}
```

### Read state — `GET /fruit-machine`

Response (200): same shape as the configure response, reflecting the current state.

### Play — `POST /fruit-machine/play`

Response (200):

```json
{
  "slots": [2, 2, 1, 0],
  "prize": "SMALL_PRIZE",
  "payout": 5,
  "freePlaysCredited": 0,
  "currentFloat": 95,
  "freePlaysBalance": 0
}
```

`prize` is one of `JACKPOT`, `FULL_HOUSE`, `SMALL_PRIZE`, `NONE`.

### Errors

Failures return a consistent body:

```json
{ "status": 400, "message": "k cannot exceed slotCount", "timestamp": "..." }
```

- Invalid request shape (missing field, value below its limit) → **400**
- Game-invariant violation (e.g. `k > slotCount`) → **400**
- Playing or reading before a machine is configured → **409**

## How to play (the rules)

The machine has a number of slots; each spin shows a random colour in every slot. A spin is
evaluated to a single prize, and the machine's **float** (its pot of money) is settled:

- **Jackpot** — all slots the same colour — pays out the entire float.
- **Full house** — all slots a different colour — pays out half the float.
- **Small prize** — a run of at least `k` adjacent slots the same colour — pays out `5 × playCost`.
- **No win** — none of the above — pays nothing.

If a (non-jackpot) prize is larger than the float can cover, the machine pays what it has and
credits the player with free plays equal to the shortfall divided by the play cost (rounded down).

## Architecture

```
src/main/java/com/example/fruitmachine/
├── FruitMachineApplication.java            Spring Boot entry point; registers the ColourSelector bean
│
├── domain/                                 The game — plain Java, no Spring
│   ├── FruitMachine.java                   The machine: spins, evaluates, settles the float
│   ├── MachineConfig.java                  Immutable config + validation (slots, colours, k, cost, float)
│   ├── SpinResult.java                     Immutable record — the colours shown
│   ├── PlayResult.java                     Immutable record — outcome of a play
│   ├── PrizeType.java                      Enum — JACKPOT, FULL_HOUSE, SMALL_PRIZE, NONE
│   ├── PrizeEvaluator.java                 Interface — contract for deciding the prize
│   ├── DefaultPrizeEvaluator.java          Implementation — precedence + run-of-k detection
│   ├── ColourSelector.java                 Interface — the randomness seam
│   ├── RandomColourSelector.java           Implementation — random colours for production
│   └── InvalidMachineConfigException.java  Typed exception for bad configuration
│
├── service/                                State + orchestration
│   ├── FruitMachineService.java            Holds the machine between calls; synchronized
│   └── MachineNotConfiguredException.java  Typed exception — played before configuring
│
└── web/                                    HTTP layer — no game logic
    ├── FruitMachineController.java         REST endpoints (configure, state, play)
    ├── dto/
    │   ├── ConfigureMachineRequest.java    Request body + bean validation
    │   ├── MachineStateResponse.java       Response — config + float + free plays
    │   └── PlayResponse.java               Response — spin outcome
    └── error/
        ├── ApiError.java                   Consistent error body
        └── GlobalExceptionHandler.java     Maps exceptions to HTTP status codes
```

The three layers keep the game logic readable and testable on its own:

- **`domain`** — the game itself, with no knowledge of Spring or HTTP.
- **`service`** — holds the single in-memory machine between calls and orchestrates the domain.
  Methods are `synchronized`, so simultaneous requests can't corrupt the shared float and
  free-play counts.
- **`web`** — controller, DTOs, validation, and centralised error handling; no game logic.

Notable choices:

- **Colours are integer ids** (0 .. colourCount-1), not an enum, so hundreds of colours cost nothing.
- **Randomness sits behind `ColourSelector`**, so spins are deterministic in tests.
- **Prize rules live behind `PrizeEvaluator`**, so a new or changed rule doesn't touch the machine.
- **State is a single in-memory machine** held in the service, replaced whenever it is reconfigured.
- **Money is a whole-unit `long`.**

## Decisions on the ambiguous parts of the brief

Where the brief was deliberately open, these are the calls I made.

- **Prize precedence:** Jackpot > Full house > Small prize > None — the single best prize is
  returned. The only real overlap is jackpot vs small prize (all-same is also a run), which
  resolves in the player's favour.
- **Small prize = a run of *at least* k** adjacent same-colour slots.
- **Full house = all slots distinct.**
- **Half the float is rounded down,** on an odd float.
- **The play cost is not added to the float.** The float is a prize pool that only drains via
  payouts. This is what gives the "float too small" rule meaning - otherwise the pot would rarely
  run low and free plays would almost never be credited. (The alternative - feeding stakes into the
  pot like a real machine - I chose not to take for this assignment.)
- **Free plays credited = the shortfall/playCost.** When a prize can't be fully paid, the unpaid amount is converted into whole free plays by dividing by the play cost. 
  Free plays are tracked, redeeming them (playing without paying) is out of scope.
  credited as free plays. Free plays are tracked; redeeming them (playing without paying) is out of
  scope.
- **Config limits — a machine that can't produce a real game is rejected:**
  - **slotCount ≥ 3.** With 1 or 2 slots every spin wins, so there are no losers. 3 is the smallest size where a loss is
    possible.
  - **k ≥ 2.** With k = 1 every slot is a trivial run, so every spin wins a small prize — the same
    "no losers" problem.
  - **k ≤ slotCount.** k = slotCount is allowed (a run spanning all slots); a k larger than the
    slot count is impossible, so it's rejected.

  The brief sets no minimums, so these are judgement calls.

## Testing

- **Domain and service — unit tests** (fast, no Spring). Randomness is injected, so every prize,
  payout, edge case, and the k generalisation is asserted deterministically.
- **Web — integration tests** (`@SpringBootTest` + MockMvc) that check wiring, validation, and
  error mapping through real HTTP — not the game rules, which the unit tests already cover.

Run them with `mvn test`.

## Left out / with more time

- Persistence (state is in memory and lost on restart) and support for multiple machines by id.
- Redeeming free plays.
- Concurrency beyond `synchronized` on the service.
- A broader error-handling net (a logged catch-all for unexpected errors) and OpenAPI/Swagger docs.

## AI usage

See [`AI_USAGE.md`](./AI_USAGE.md).