---
name: "SDLC Step 05 Implementation Instructions"
description: "Use when executing impl-plan.md, writing production Python, changing files under dev, or implementing SDLC Phase 5 tasks."
---
# SDLC Step 05: Implementation

- Read `impl-plan.md` and `architecture.md` before editing.
- Execute tasks in plan order and keep changes limited to the requested task scope.
- Production code must be Python and must live under `dev/`.
- Do not write tests, edit pipeline artifacts, or place code outside `dev/`.
- Validate external input, keep secrets in environment variables, and follow OWASP-aligned practices.
- Preserve existing public interfaces unless the plan explicitly requires a contract change.
- Run the narrowest available checks after implementation and report commands and actual outcomes.
- Report completed, skipped, and blocked tasks with their target files and any deviations from the plan.
