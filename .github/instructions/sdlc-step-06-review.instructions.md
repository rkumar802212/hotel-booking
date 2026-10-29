---
name: "SDLC Step 06 Code Review Instructions"
description: "Use when reviewing implemented files under dev for correctness, security, quality, plan conformance, regressions, or performing SDLC Phase 6 safe fixes."
---
# SDLC Step 06: Code Review

- Read `impl-plan.md` and `architecture.md` before reviewing the implementation under `dev/`.
- Lead with concrete findings ordered by severity; include file references and the affected behavior.
- Check correctness, input validation, error handling, security, maintainability, and conformance to the plan.
- Treat missing or weak verification as a review risk, not as evidence that behavior works.
- Apply only small, clearly safe fixes that are directly supported by the review.
- Do not redesign the system, add unrelated features, or write verification tests in this phase.
- Clearly separate fixed findings, remaining findings, assumptions, and test gaps.
- Return a review outcome that the orchestrator can use before Phase 7.
