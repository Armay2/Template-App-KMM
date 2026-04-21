import SwiftUI
import Shared

struct TodoDetailActions {
    let onTitleChange: (String) -> Void
    let onDescriptionChange: (String) -> Void
    let onToggle: () -> Void
    let onSave: () -> Void
    let onDelete: () -> Void
}

struct TodoDetailView: View {
    let state: TodoDetailState
    let actions: TodoDetailActions
    @State private var showDeleteConfirm = false

    var body: some View {
        Form {
            Section("Title") {
                TextField("Title", text: Binding(
                    get: { state.title },
                    set: actions.onTitleChange,
                ))
                .font(.title3)
            }
            Section("Description") {
                TextField("Description", text: Binding(
                    get: { state.description_ },
                    set: actions.onDescriptionChange,
                ), axis: .vertical)
                .lineLimit(4...10)
            }
            Section {
                Toggle("Completed", isOn: Binding(
                    get: { state.done },
                    set: { _ in actions.onToggle() },
                ))
            }
            Section {
                Button("Save", action: actions.onSave)
                    .frame(maxWidth: .infinity)
                    .buttonStyle(.borderedProminent)
                if state.id != nil {
                    Button("Delete", role: .destructive) {
                        showDeleteConfirm = true
                    }
                    .frame(maxWidth: .infinity)
                }
            }
        }
        .navigationTitle(state.id == nil ? "New" : "Edit")
        .alert("Delete this todo?", isPresented: $showDeleteConfirm) {
            Button("Cancel", role: .cancel) {}
            Button("Delete", role: .destructive, action: actions.onDelete)
        } message: {
            Text("This action cannot be undone.")
        }
    }
}

extension TodoDetailActions {
    static let noop = TodoDetailActions(
        onTitleChange: { _ in }, onDescriptionChange: { _ in },
        onToggle: {}, onSave: {}, onDelete: {},
    )
}

#Preview("New") {
    NavigationStack {
        TodoDetailView(
            state: TodoDetailState(id: nil, title: "", description: "", done: false, status: UiStatusIdle.shared),
            actions: .noop,
        )
    }
}

#Preview("Existing") {
    NavigationStack {
        TodoDetailView(
            state: TodoDetailState(
                id: "1", title: "Buy groceries", description: "Milk, bread, eggs",
                done: false, status: UiStatusIdle.shared,
            ),
            actions: .noop,
        )
    }
}
