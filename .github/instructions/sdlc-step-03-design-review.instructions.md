---
name: "SDLC Step 03 Design Review Instructions"
description: "Use when reviewing requirements and architecture for completeness, feasibility, security, risks, traceability, or producing design-review.md for SDLC Phase 3."
---
# SDLC Step 03: Design Review

- Read both `requirements.md` and `architecture.md` before reviewing.
- Evaluate traceability, feasibility, security, reliability, maintainability, testability, and scope alignment.
- Check that the architecture addresses every functional and non-functional requirement.
- Distinguish blocking findings from non-blocking recommendations and explain the impact of each finding.
- Produce only the root-level `design-review.md` artifact.
- End with one unambiguous verdict: `APPROVE` or `REJECT`.
- Use `REJECT` when a blocking gap prevents safe implementation; include concrete remediation guidance for Phase 2.
- Do not implement code or silently rewrite the architecture while reviewing.
- Return a short verdict summary suitable for the Phase 3 gate.
