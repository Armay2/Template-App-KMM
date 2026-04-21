import SwiftUI

struct QuickAddSheet: View {
    let onCreate: (String) -> Void
    let onDismiss: () -> Void
    @State private var title: String = ""
    @FocusState private var focused: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("NEW TODO")
                .font(.caption)
                .foregroundStyle(.secondary)
            TextField("What needs doing?", text: $title)
                .textFieldStyle(.roundedBorder)
                .focused($focused)
                .submitLabel(.done)
                .onSubmit(submit)
        }
        .padding(.horizontal, 24)
        .padding(.top, 24)
        .padding(.bottom, 12)
        .presentationDetents([.height(180)])
        .presentationDragIndicator(.visible)
        .task { focused = true }
    }

    private func submit() {
        let t = title.trimmingCharacters(in: .whitespaces)
        guard !t.isEmpty else { onDismiss(); return }
        onCreate(t)
        onDismiss()
    }
}

#Preview {
    Color.clear.sheet(isPresented: .constant(true)) {
        QuickAddSheet(onCreate: { _ in }, onDismiss: {})
    }
}
