# Fruit Machine

A virtual fruit machine exposed as a Spring Boot REST service.

> This is a scaffold. The game rules in `DefaultPrizeEvaluator` and `FruitMachine.play()` are
> stubbed with `TODO`s — implement them, then delete this note.

## How to run

Requires JDK 21+.

```bash
# run the service (http://localhost:8080)
./mvnw spring-boot:run      # or: mvn spring-boot:run

# run the tests
./mvnw test                 # or: mvn test
```

> Tip: generate the Maven wrapper once with `mvn -N wrapper:wrapper` so reviewers don't need
> Maven installed, then commit the `mvnw` / `.mvn` files.

## API

| Method | Path            | Purpose                                  |
|--------|-----------------|------------------------------------------|
| POST   | `/machine`      | Configure (or reconfigure) the machine   |
| GET    | `/machine`      | Read current state (config, float)       |
| POST   | `/machine/play` | Spin, evaluate, settle, return outcome   |

Configure body:

```json
{ "slotCount": 4, "colourCount": 4, "k": 2, "playCost": 1, "startingFloat": 100 }
```

## Design

The code is split into three layers so the game logic can be understood and tested on its own:

- **`domain`** — pure game logic, no Spring. `FruitMachine`, `MachineConfig`, `PrizeEvaluator`,
  and the `ColourSelector` randomness seam.
- **`service`** — `MachineService` holds state between calls and orchestrates the domain.
- **`web`** — controller, DTOs, validation, and centralised error handling. No rules here.

Key decisions (starting points — change any you'd defend differently, and record why):

- **Colours as integer ids**, not an enum, so hundreds of colours (Part 3) cost nothing.
- **Randomness behind `ColourSelector`** so spins are deterministic in tests.
- **Rules behind `PrizeEvaluator`** so a new/changed rule doesn't touch the machine.
- **State is a single in-memory machine** in the service, replaced on reconfigure.
- **Money as `long`** for now (revisit for rounding / currency).

## Decision log

<!-- Record the ambiguities you resolved and how. The brief expects this. Examples: -->
- Small prize = a run of **at least** k adjacent same-colour slots (vs exactly k). [decide]
- Precedence when a spin matches multiple rules (a jackpot also contains runs). [decide]
- Whether `playCost` is deducted from / added to the float, and how free plays are consumed.
- Rounding of "half the float" when the float is odd.

## Left out / with more time

- <!-- e.g. persistence, multiple machines, concurrency strategy, OpenAPI, Docker, CI -->

## AI usage

See [`AI_USAGE.md`](./AI_USAGE.md).
