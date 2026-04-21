import SwiftUI
import Shared

struct TodoListActions {
    let onRefresh: () -> Void
    let onCreate: () -> Void
    let onSelect: (String) -> Void
    let onToggle: (String) -> Void
    let onDelete: (String) -> Void
    let onToggleDoneSection: () -> Void
}

struct TodoListView: View {
    let state: TodoListState
    let actions: TodoListActions

    var body: some View {
        Group {
            if state.todos.isEmpty && state.status is UiStatusLoading {
                ProgressView()
            } else if state.todos.isEmpty {
                TodoEmptyStateView()
            } else {
                List {
                    if state.active.isEmpty {
                        Text("All caught up! 🎉")
                            .font(.title3)
                            .frame(maxWidth: .infinity)
                            .listRowSeparator(.hidden)
                            .padding(.vertical, 24)
                    } else {
                        ForEach(state.active, id: \.id) { t in
                            row(for: t)
                        }
                    }
                    if !state.done.isEmpty {
                        Section {
                            if state.isDoneExpanded {
                                ForEach(state.done, id: \.id) { t in
                                    row(for: t)
                                }
                            }
                        } header: {
                            TodoSectionHeader(
                                title: "Done",
                                count: Int(state.done.count),
                                expanded: state.isDoneExpanded,
                                onToggle: actions.onToggleDoneSection,
                            )
                        }
                    }
                }
                .listStyle(.plain)
                .animation(.spring, value: state.todos)
                .animation(.spring, value: state.isDoneExpanded)
                .refreshable { actions.onRefresh() }
            }
        }
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                Button {
                    UIImpactFeedbackGenerator(style: .medium).impactOccurred()
                    actions.onCreate()
                } label: { Image(systemName: "plus") }
            }
        }
        .navigationTitle("Todos")
    }

    @ViewBuilder
    private func row(for todo: Todo) -> some View {
        TodoRowView(todo: todo, onToggle: actions.onToggle, onSelect: actions.onSelect)
            .swipeActions(edge: .trailing, allowsFullSwipe: true) {
                Button(role: .destructive) {
                    UINotificationFeedbackGenerator().notificationOccurred(.warning)
                    actions.onDelete(todo.id)
                } label: {
                    Label("Delete", systemImage: "trash")
                }
            }
    }
}

extension TodoListActions {
    static let noop = TodoListActions(
        onRefresh: {}, onCreate: {}, onSelect: { _ in }, onToggle: { _ in },
        onDelete: { _ in }, onToggleDoneSection: {},
    )
}

#Preview("Empty") {
    NavigationStack {
        TodoListView(
            state: TodoListState(todos: [], isDoneExpanded: false, status: UiStatusIdle.shared),
            actions: .noop,
        )
    }
}

#Preview("With items") {
    let items = [
        Todo(id: "1", title: "Buy groceries", description: "", done: false),
        Todo(id: "2", title: "Walk the dog", description: "", done: true),
    ]
    return NavigationStack {
        TodoListView(
            state: TodoListState(todos: items, isDoneExpanded: false, status: UiStatusIdle.shared),
            actions: .noop,
        )
    }
}

#Preview("With Done expanded") {
    let items = [
        Todo(id: "1", title: "Buy groceries", description: "", done: false),
        Todo(id: "2", title: "Book dentist", description: "", done: false),
        Todo(id: "3", title: "Walk the dog", description: "", done: true),
        Todo(id: "4", title: "Renew passport", description: "", done: true),
    ]
    return NavigationStack {
        TodoListView(
            state: TodoListState(todos: items, isDoneExpanded: true, status: UiStatusIdle.shared),
            actions: .noop,
        )
    }
}
