import SwiftUI
import Shared

struct TodoListActions {
    let onRefresh: () -> Void
    let onCreate: () -> Void
    let onSelect: (String) -> Void
    let onToggle: (String) -> Void
}

struct TodoListView: View {
    let state: TodoListState
    let actions: TodoListActions

    var body: some View {
        Group {
            if state.todos.isEmpty && state.status is UiStatusLoading {
                ProgressView()
            } else if state.todos.isEmpty {
                ContentUnavailableView("No todos yet.", systemImage: "checklist")
            } else {
                List(state.todos, id: \.id) { t in
                    TodoRowView(todo: t, onToggle: actions.onToggle, onSelect: actions.onSelect)
                }
                .refreshable { actions.onRefresh() }
            }
        }
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                Button(action: actions.onCreate) { Image(systemName: "plus") }
            }
        }
        .navigationTitle("Todos")
    }
}

extension TodoListActions {
    static let noop = TodoListActions(onRefresh: {}, onCreate: {}, onSelect: { _ in }, onToggle: { _ in })
}

#Preview("Empty") {
    NavigationStack {
        TodoListView(
            state: TodoListState(todos: [], status: UiStatusIdle.shared),
            actions: .noop
        )
    }
}

#Preview("With items") {
    let items: [Todo] = [
        Todo(id: "1", title: "Buy groceries", description: "", done: false),
        Todo(id: "2", title: "Walk the dog", description: "", done: true),
    ]
    return NavigationStack {
        TodoListView(
            state: TodoListState(todos: items, status: UiStatusIdle.shared),
            actions: .noop
        )
    }
}
