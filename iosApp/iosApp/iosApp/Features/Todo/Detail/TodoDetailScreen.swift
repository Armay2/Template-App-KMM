import SwiftUI
import Shared

/// Container view for the Todo detail/edit screen. Resolves the Kotlin
/// `TodoDetailViewModel` (parametrised by optional id) via `KoinBridge`,
/// wraps it with `Observed` for SwiftUI, and forwards side-effects to the
/// `AppNavigator` in the environment.
struct TodoDetailScreen: View {
    let id: String?
    @Environment(AppNavigator.self) private var navigator
    @State private var observed: Observed<TodoDetailState, TodoDetailSideEffect, TodoDetailViewModel>

    init(id: String?) {
        self.id = id
        let vm = KoinBridge.shared.todoDetailViewModel(id: id)
        _observed = State(wrappedValue: Observed(vm) { _ in })
    }

    var body: some View {
        TodoDetailView(
            state: observed.state,
            actions: TodoDetailActions(
                onTitleChange: { v in observed.vm.onTitleChange(v: v) },
                onDescriptionChange: { v in observed.vm.onDescriptionChange(v: v) },
                onToggle: { observed.vm.onToggle() },
                onSave: { observed.vm.onSave() },
                onDelete: { observed.vm.onDelete() }
            )
        )
        .task { await listen() }
    }

    @MainActor private func listen() async {
        for await effect in observed.vm.effects {
            switch effect {
            case is TodoDetailSideEffectDismiss:
                navigator.pop()
            default:
                break
            }
        }
    }
}
