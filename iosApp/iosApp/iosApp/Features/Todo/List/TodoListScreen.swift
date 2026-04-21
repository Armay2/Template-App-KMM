import SwiftUI
import Shared

/// Container view for the Todo list. Resolves the Kotlin `TodoListViewModel`
/// via `KoinBridge`, wraps it with `Observed` for SwiftUI, and forwards
/// side-effects to the `AppNavigator` in the environment.
struct TodoListScreen: View {
    @Environment(AppNavigator.self) private var navigator
    @State private var observed: Observed<TodoListState, TodoListSideEffect, TodoListViewModel>

    init() {
        let vm = KoinBridge.shared.todoListViewModel()
        _observed = State(wrappedValue: Observed(vm) { _ in })
    }

    var body: some View {
        TodoListView(
            state: observed.state,
            actions: TodoListActions(
                onRefresh: { observed.vm.onRefresh() },
                onCreate: { observed.vm.onCreateNew() },
                onSelect: { id in observed.vm.onSelect(id: id) },
                onToggle: { id in observed.vm.onToggle(id: id) }
            )
        )
        .task { await listen() }
    }

    @MainActor private func listen() async {
        for await effect in observed.vm.effects {
            switch effect {
            case let e as TodoListSideEffectNavigateToDetail:
                navigator.push(.todoDetail(id: e.id))
            default:
                break
            }
        }
    }
}
