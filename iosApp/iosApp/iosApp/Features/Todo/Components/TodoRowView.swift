import SwiftUI
import Shared

struct TodoRowView: View {
    let todo: Todo
    let onToggle: (String) -> Void
    let onSelect: (String) -> Void

    var body: some View {
        HStack {
            Button { onToggle(todo.id) } label: {
                Image(systemName: todo.done ? "checkmark.circle.fill" : "circle")
            }
            Text(todo.title)
                .strikethrough(todo.done)
            Spacer()
        }
        .contentShape(Rectangle())
        .onTapGesture { onSelect(todo.id) }
    }
}

#Preview {
    TodoRowView(
        todo: Todo(id: "1", title: "Buy milk", description: "", done: false),
        onToggle: { _ in },
        onSelect: { _ in }
    )
}
