import Foundation
import Observation
import Shared

/// Generic SwiftUI-observable wrapper around a Kotlin `BaseViewModel`.
///
/// Subscribes to the Kotlin `state` StateFlow and mirrors its value into an
/// `@Observable`-tracked property on the main actor. Also forwards one-shot
/// items from the `effects` SharedFlow to `onEffect`.
///
/// Both flows are bridged by SKIE into Swift `AsyncSequence`s, so we can
/// drive them with plain `for await` loops. Tasks are cancelled on deinit.
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
