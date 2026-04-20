import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        KoinInitializer().doInit()
    }

    var body: some Scene {
        WindowGroup {
            RootView()
        }
    }
}
