import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinInitializerKt.initializeKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}