import SwiftUI

struct TodoSectionHeader: View {
    let title: String
    let count: Int
    let expanded: Bool
    let onToggle: () -> Void

    var body: some View {
        Button(action: {
            UISelectionFeedbackGenerator().selectionChanged()
            onToggle()
        }) {
            HStack(spacing: 6) {
                Image(systemName: "chevron.down")
                    .font(.caption.weight(.semibold))
                    .rotationEffect(.degrees(expanded ? 0 : -90))
                    .animation(.easeInOut(duration: 0.2), value: expanded)
                Text(title.uppercased())
                    .font(.footnote.weight(.semibold))
                    .foregroundStyle(.secondary)
                Text("(\(count))")
                    .font(.footnote)
                    .foregroundStyle(.tertiary)
                Spacer()
            }
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .listRowInsets(EdgeInsets(top: 16, leading: 16, bottom: 8, trailing: 16))
    }
}

#Preview {
    List {
        TodoSectionHeader(title: "Done", count: 3, expanded: false, onToggle: {})
        TodoSectionHeader(title: "Done", count: 3, expanded: true, onToggle: {})
    }
}
