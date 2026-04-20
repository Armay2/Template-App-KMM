import SwiftUI

/// Installs `navigationDestination(for: Route.self)` on its content so that
/// any `NavigationStack` ancestor can resolve pushes emitted by
/// `AppNavigator.push(_:)`.
struct DestinationRouter: ViewModifier {
    func body(content: Content) -> some View {
        content.navigationDestination(for: Route.self) { route in
            switch route {
            case .todoList:
                TodoListScreen()
            case .todoDetail(let id):
                TodoDetailScreen(id: id)
            }
        }
    }
}

extension View {
    func destinationRouting() -> some View { modifier(DestinationRouter()) }
}
