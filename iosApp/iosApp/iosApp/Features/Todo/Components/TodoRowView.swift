import SwiftUI
import Shared

struct TodoRowView: View {
    let todo: Todo
    let onToggle: (String) -> Void
    let onSelect: (String) -> Void

    var body: some View {
        HStack(spacing: 12) {
            Button {
                UISelectionFeedbackGenerator().selectionChanged()
                onToggle(todo.id)
            } label: {
                Image(systemName: todo.done ? "checkmark.circle.fill" : "circle")
                    .foregroundStyle(todo.done ? .secondary : .primary)
            }
            .buttonStyle(.plain)
            Text(todo.title)
                .strikethrough(todo.done)
                .foregroundStyle(todo.done ? .secondary : .primary)
            Spacer()
        }
        .contentShape(Rectangle())
        .onTapGesture { onSelect(todo.id) }
    }
}

#Preview {
    List {
        TodoRowView(
            todo: Todo(id: "1", title: "Buy milk", description: "", done: false),
            onToggle: { _ in },
            onSelect: { _ in },
        )
        TodoRowView(
            todo: Todo(id: "2", title: "Walk the dog", description: "", done: true),
            onToggle: { _ in },
            onSelect: { _ in },
        )
    }
}
