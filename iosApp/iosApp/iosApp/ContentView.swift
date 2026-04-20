import SwiftUI
import Shared

/// Root scene of the app. Owns the `AppNavigator` and hosts the
/// `NavigationStack`. Feature screens live below `.destinationRouting()`.
struct RootView: View {
    @State private var navigator = AppNavigator()

    var body: some View {
        NavigationStack(path: $navigator.path) {
            TodoListScreen()
                .destinationRouting()
        }
        .environment(navigator)
    }
}

#Preview {
    RootView()
}
