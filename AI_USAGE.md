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
