import SwiftUI

struct TodoEmptyStateView: View {
    var body: some View {
        VStack(spacing: 12) {
            Image(systemName: "checklist")
                .font(.system(size: 72))
                .foregroundStyle(.quaternary)
            Text("Nothing to do.")
                .font(.title3.weight(.semibold))
            Text("Tap + to create your first todo.")
                .font(.subheadline)
                .foregroundStyle(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding(24)
    }
}

#Preview { TodoEmptyStateView() }
