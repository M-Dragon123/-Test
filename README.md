# 📱 Simple Android Calculator (Kotlin + Compose)

A modern, clean Android calculator app built with **Kotlin** and **Jetpack Compose**.

Beyond basic arithmetic, this project serves as a demonstration of **Clean Architecture**, **MVVM pattern**, and advanced **Testing Strategies** (including the use of Test Doubles: Fakes, Stubs, and Dummies).

## ⚠️ IMPORTANT: EMULATOR CONFIGURATION (非常重要)

### 🛑 DO NOT use API 36 (Android 16 Preview)
**请不要使用 API 36 (Android 16 预览版) 的模拟器运行本项目。**

Due to compatibility issues with current Espresso testing libraries (specifically `InputManager` APIs), running tests on API 36 will cause crashes.

### ✅ RECOMMENDATION
**Please use API 34 (Android 14) or API 33.**
**请务必使用 API 34 (Android 14) 或 API 33 的模拟器/真机。**

---

## ✨ Features

* **Standard Operations:** Addition, Subtraction, Multiplication, Division.
* **Advanced Functions:** Square Root (`sqrt`) and Percentage (`%`).
* **Smart Input:**
    * Supports decimal points.
    * **Negative Number Logic:** Context-aware minus sign (e.g., can input `5 + -2`).
* **Reactive UI:** Real-time equation display (e.g., shows "12 + 5" while typing) using Jetpack Compose.

## 🛠 Tech Stack

* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material3)
* **Architecture:** MVVM (Model-View-ViewModel)
* **State Management:** Kotlin Coroutines & StateFlow
* **Build System:** Gradle (Kotlin DSL) with Version Catalog (`libs.versions.toml`)

## 🏗 Architecture & Design

The project follows the **Separation of Concerns** principle:

1.  **`ICalculator` (Interface):** Defines the contract for calculation logic. This allows for easy swapping of implementations.
2.  **`RealCalculator` (Model):** The production implementation of the math logic.
3.  **`CalculatorViewModel` (ViewModel):** Manages the UI state (`CalculatorUiState`), handles user events, and holds business logic for input parsing.
4.  **`MainActivity` (View):** Pure UI layer built with Composable functions, observing the ViewModel state.

## 🧪 Testing Strategy

This project places a heavy emphasis on testing methodologies.

### 1. Unit Tests (Local)
Located in `app/src/test/`.
We use **Test Doubles** to isolate the `ViewModel` from the concrete calculation logic.

* **Fake (FakeCalculator):** A working implementation of the calculator that runs in memory. Used to verify complex state logic (e.g., ensuring `5 + -2 = 3`).
* **Stub (StubCalculator):** Hard-coded responses (e.g., `add` always returns `999`). Used to verify that the ViewModel correctly propagates data from the model to the UI.
* **Dummy (DummyCalculator):** An object that throws exceptions if used. Used to satisfy constructor dependencies when the calculator logic itself isn't the focus of the test.

### 2. Instrumented UI Tests (Device)
Located in `app/src/androidTest/`.
Uses `ComposeTestRule` and `Espresso` to simulate user interactions on an Emulator.

* **Flow Testing:** Simulates clicking buttons (`5`, `+`, `7`, `=`) and verifies the screen displays the correct result (`12`).
* **Tagging:** Uses `testTag` modifiers to strictly identify UI components.

## 🚀 Getting Started

1.  Clone the repository:
    ```bash
    git clone [https://github.com/M-Dragon123/-Test.git](https://github.com/M-Dragon123/-Test.git)
    ```
2.  Open the project in **Android Studio**.
3.  **Ensure you are using an Emulator with API 34.**
4.  Sync Gradle files.
5.  Run the app.

## 📸 Screenshots
<img width="342" height="581" alt="image" src="https://github.com/user-attachments/assets/21e1136b-85d9-499d-9b29-c0a2f1ecb869" />


**Author:** [M-Dragon123](https://github.com/M-Dragon123)
