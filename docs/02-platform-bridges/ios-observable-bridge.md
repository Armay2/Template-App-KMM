# iOS `Observed` bridge

After reading this you'll know how a Kotlin `BaseViewModel` is consumed as a SwiftUI `@Observable` value, and why `isolated deinit` is used.

## The wrapper

SKIE bridges `StateFlow` and `SharedFlow` to Swift `AsyncSequence`s, but SwiftUI still needs `@Observable` tracking on the main actor. The generic `Observed` wrapper at `iosApp/iosApp/iosApp/Presentation/Base/Observed.swift` does exactly that:

```swift
import Foundation
import Observation
import Shared

@MainActor
@Observable
final class Observed<State: AnyObject,
                     Effect: AnyObject,
                     VM: BaseViewModel<State, Effect>> {
    let vm: VM
    private(set) var state: State
    private var tasks: [Task<Void, Never>] = []

    init(_ vm: VM, onEffect: @escaping @MainActor (Effect) -> Void) {
        self.vm = vm
        self.state = vm.state.value
        tasks.append(Task { @MainActor [weak self, vm] in
            for await s in vm.state { self?.state = s }
        })
        tasks.append(Task { @MainActor [vm] in
            for await e in vm.effects { onEffect(e) }
        })
    }

    // Swift 6: `deinit` is nonisolated by default. We opt into isolation
    // on the main actor so we can safely read `tasks` and cancel each one.
    // See SE-0371 (Isolated deinit).
    isolated deinit {
        for task in tasks { task.cancel() }
    }
}
```

## How to use it

Container views resolve the Kotlin VM through `KoinBridge` and wrap it as `@State`. Example from `Features/Todo/List/TodoListScreen.swift`:

```swift
@State private var observed: Observed<TodoListState, TodoListSideEffect, TodoListViewModel>

init() {
    let vm = KoinBridge.shared.todoListViewModel()
    _observed = State(wrappedValue: Observed(vm) { _ in })
}
```

Intent forwarding goes through `observed.vm` directly (`observed.vm.onRefresh()`), state is read from `observed.state`, and side-effect handling is either done in the `onEffect` closure or, for interaction with the environment (e.g. `navigator.push`), via a dedicated `.task { for await effect in observed.vm.effects { ... } }`.

## Swift 6 notes

- The class is marked `@MainActor @Observable` so SwiftUI change tracking happens on the correct isolation domain.
- `isolated deinit` is required because Swift 6's default `nonisolated deinit` cannot touch actor-isolated stored properties. SE-0371 added the `isolated` modifier specifically for this case.
- The tasks capture `vm` non-weakly (ViewModels live as long as the screen) but `self` weakly to avoid a cycle during the `state` loop.
