## 🤖AI usage log

As encouraged on brief, I used AI assistance (Specifically Claude) selectively and transparently. The principle I followed: AI handles structural boilerplate where there is no design decision to be made, I write everything where judgment, logic, or reasoning is involved.

### What AI generated

* The project scaffold: structure, pom.xml, package layout, and initial stubbed classes.
* The boilerplate around the game: the request/response DTOs, the ApiError type, and the
  GlobalExceptionHandler.
* The controller’s endpoint scaffolding — the method signatures, annotations, and
  request/response wiring, with the bodies left as TODOs.
* The test helpers (ScriptedColourSelector and RecordingSelector) and the first drafts of
  the basic test cases.
* The Docker setup (Dockerfile, compose, dockerignore).
* Drafts of this document and the README, which I then edited.

### What I owned

* The core game logic: the spin, working out the prize, and the payout / free-play maths.
* All the decisions where the brief was vague (prize precedence, the run-of-k rule, rounding,
  how the pot and free plays work, and the validation limits). The reasoning for each is in the
  README’s decision log.
* Extra tests beyond the AI-generated basics: the edge cases and the config-validation tests.
* Filling in the controller bodies: validating the input, calling the service, and mapping the
  result to a response.
* Catching and fixing bugs in the AI’s output.

### What worked well (and the prompts I used)
* I took help of AI to build a skeleton that took care of setting up boilerplate - project structure, pom.xml, DTOs, error handlers, test helpers and basic testcases. A prompt like
````
Scaffolding was a few prompts, not one. I started with the brief and give me a Spring Boot skeleton - controller, DTOs, exception handler - with the method bodies left as TODOs so I write the game logic myself. 
From there it was corrections: move validation into the domain and keep the DTO to shape checks only, and no game logic in the controller - just validate, call the service, map the response. 
Basically keeping the logic out on purpose meant I wrote the game rules, not the AI.
````
* Use AI as a code reviewer. Once the implementation was done, I asked AI to review it. This pointed me at real problems (below) faster than I'd have found them alone.
````
Review this against the brief provided. What edge cases have I missed, testcases missed.
````

### Where the AI got it wrong, and how I steered it

These are places the AI's output was wrong or messy, and what I did about it.

* Free plays - the big one. The AI credited free plays equal to the leftover money. I didn't agree - a free play is a number of spins, not an amount of money. I pushed back:
* "A free play is a count of spins, not money. Shouldn't this be shortfall ÷ play cost?"
* It then gave me a formula, (shortfall + playCost) / playCost, that counted one too many on exact amounts (a shortfall of 15 at a cost of 5 gave 4 free plays, not 3) and even gave a free play when nothing was owed. A failing test caught it. I switched to plain shortfall / playCost (round down) as the simpler choice and wrote down why in the README.
* Divide-by-zero. That same line crashes if playCost is 0, which my config still allowed. I fixed the rule to playCost >= 1 and made the DTO, the domain check, and the docs all agree.
* Leftover junk in the tests. The AI-written tests had unused imports from a concurrency test that was never finished. Some were duplicate tests. I deleted such dead code.
* Docs out of sync. The AI's first drafts of the README and this file didn't match the code - at one point the README still described the old money-based rule after I'd changed it. I rewrote them to match.

