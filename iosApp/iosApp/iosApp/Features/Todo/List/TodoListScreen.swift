import SwiftUI
import Shared

struct TodoListScreen: View {
    @Environment(AppNavigator.self) private var navigator
    @State private var observed: Observed<TodoListState, TodoListSideEffect, TodoListViewModel>
    @State private var quickAddPresented: Bool = false

    init() {
        let vm = KoinBridge.shared.todoListViewModel()
        _observed = State(wrappedValue: Observed(vm) { _ in })
    }

    var body: some View {
        TodoListView(
            state: observed.state,
            actions: TodoListActions(
                onRefresh: observed.vm.onRefresh,
                onCreate: observed.vm.onRequestQuickAdd,
                onSelect: { id in observed.vm.onSelect(id: id) },
                onToggle: { id in observed.vm.onToggle(id: id) },
                onDelete: { id in observed.vm.onDelete(id: id) },
                onToggleDoneSection: observed.vm.onToggleDoneSection,
            ),
        )
        .sheet(isPresented: $quickAddPresented) {
            QuickAddSheet(
                onCreate: { title in observed.vm.onQuickAdd(title: title) },
                onDismiss: { quickAddPresented = false },
            )
        }
        .task { await listen() }
    }

    @MainActor private func listen() async {
        for await effect in observed.vm.effects {
            switch effect {
            case let e as TodoListSideEffectNavigateToDetail:
                navigator.push(.todoDetail(id: e.id))
            case is TodoListSideEffectOpenQuickAdd:
                quickAddPresented = true
            default:
                break
            }
        }
    }
}
