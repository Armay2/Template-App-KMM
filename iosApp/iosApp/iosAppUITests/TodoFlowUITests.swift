import XCTest

final class TodoFlowUITests: XCTestCase {
    func testListRendersAndFabShows() throws {
        let app = XCUIApplication()
        app.launch()
        XCTAssertTrue(app.navigationBars["Todos"].waitForExistence(timeout: 10))
    }
}
