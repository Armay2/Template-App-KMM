# ADR 0001: Share code up to (and including) the ViewModel

- **Status:** Accepted
- **Date:** 2026-04-21

## Context

A multiplatform template has to pick a line between "what we share" and "what's native". The practical options were: share only `domain` + `data`; share up to and including the ViewModel (MVVM + MVI-lite); or share the UI too with Compose Multiplatform.

Pure domain/data sharing duplicates view-state reducers, side-effect handling, validation, and error mapping across platforms — the exact places where consistency bugs hurt users most. Compose Multiplatform on iOS is still uneven for iOS 26 polish (Liquid Glass, native gestures, accessibility), and commits us to one toolkit for everything forever.

## Decision

We share everything from `core/`, `data/`, `domain/`, and `presentation/` — including `BaseViewModel`, state types, side-effect types, and feature ViewModels — but leave rendering, navigation hosting, and DI bootstrap native. The boundary is the ViewModel: its inputs are Kotlin, its outputs (`StateFlow<State>`, `SharedFlow<SideEffect>`) are consumed natively. SKIE makes those flows feel like Swift `AsyncSequence`; Compose reads them via `collectAsStateWithLifecycle`.

## Consequences

**Positive.** One test suite (`commonTest`) covers state logic, validation, and side-effects for both apps. Designers get Compose and SwiftUI native affordances. Bugs in shared code get fixed in one place. iOS team can use Swift 6 / Liquid Glass without fighting a Kotlin UI layer.

**Negative.** Each feature has a small duplication: one `Screen.kt` + one `Screen.swift`. Changes to the state contract force both platforms to recompile. New engineers must learn Kotlin + Swift + one SwiftUI interop wrapper (`Observed`). Net: we accept two thin views in exchange for one shared everything-else.
