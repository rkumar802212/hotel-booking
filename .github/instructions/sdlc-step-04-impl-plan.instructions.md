---
name: "SDLC Step 04 Implementation Plan Instructions"
description: "Use when decomposing approved architecture into ordered implementation tasks, defining file targets or Definition of Done, or producing impl-plan.md for SDLC Phase 4."
---
# SDLC Step 04: Implementation Plan

- Read `requirements.md`, `architecture.md`, and the approved `design-review.md` before planning.
- Produce only the root-level `impl-plan.md` artifact.
- Create ordered, independently understandable tasks with stable IDs such as `TASK-01`.
- For each task, specify target files, implementation intent, dependencies, validation, and a measurable Definition of Done.
- Preserve requirement traceability by linking tasks to `FR-*`, `NFR-*`, or acceptance criteria IDs.
- Keep production code under `dev/` and Playwright plus TypeScript verification under `test-automation/`.
- Do not write application code or tests during planning.
- Resolve design-review findings before marking the plan ready for Phase 5.
- Return task count, dependencies, and any remaining blockers for the Phase 4 gate.
