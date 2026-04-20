# Contributing

## Branches
- `main` is always shippable. Never commit directly.
- Feature branches: `feat/<scope>`, `fix/<scope>`, `docs/<scope>`, `chore/<scope>`.

## Commits
Conventional Commits: `feat(todo): add detail screen`.

## Before opening a PR
- `./gradlew detekt ktlintCheck test`
- `swiftlint` and `swiftformat --lint iosApp`
- Verify both `androidApp` and `iosApp` build locally.

## PR checklist
See `docs/03-adding-a-feature/checklist.md`.
