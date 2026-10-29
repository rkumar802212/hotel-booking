---
name: "SDLC Step 02 Architecture Instructions"
description: "Use when designing system architecture from requirements, selecting components or technology, documenting data flow, or producing architecture.md for SDLC Phase 2."
---
# SDLC Step 02: Architecture

- Read `requirements.md` completely before making design decisions.
- Produce only the root-level `architecture.md` artifact.
- Map every in-scope requirement to one or more components, interfaces, or flows.
- Document component responsibilities, data flow, deployment shape, technology choices, and key security or operational risks.
- Include a Mermaid component or data-flow diagram when it improves clarity.
- Record consequential choices as ADRs with the decision and rationale.
- Respect the existing repository structure and the pipeline rule that production Python belongs under `dev/` and verification TypeScript belongs under `test-automation/`.
- Do not implement application code, tests, or a design-review verdict.
- Call out unresolved assumptions so Phase 3 can review them explicitly.
