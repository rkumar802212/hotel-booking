---
name: "SDLC Step 07 Verification Instructions"
description: "Use when writing or running Playwright and TypeScript acceptance tests, validating dev behavior, or producing verification-report.md for SDLC Phase 7."
---
# SDLC Step 07: Verification

- Read `requirements.md`, `impl-plan.md`, and the implementation under `dev/` before writing tests.
- Put all verification code under `test-automation/` and use TypeScript with Playwright.
- Cover each acceptance criterion and task Definition of Done, including at least one error path where applicable.
- Do not change application code to make a test pass; fix only test code when the failure is caused by the test.
- Never claim PASS unless the test command actually ran and produced that result.
- If the environment prevents execution, report the limitation and exact reproduction command.
- Keep the verification report traceable to requirement and task IDs, with totals, failures, coverage, and commands.
- Respect the maximum of two test-fix-and-rerun cycles before escalating a blocker to the orchestrator.
