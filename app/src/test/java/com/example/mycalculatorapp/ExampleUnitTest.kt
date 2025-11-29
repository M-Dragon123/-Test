package com.example.mycalculatorapp

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * This is our unit test file.
 * It runs on your local machine (not on an emulator) and is very fast.
 */
class ExampleUnitTest {

    // SUT = System Under Test
    private lateinit var viewModel: CalculatorViewModel

    // Test doubles
    private lateinit var calculator: ICalculator

    @Test
    fun `test ViewModel - using FakeCalculator - 5 + 7 should equal 12`() = runBlocking {
        // 1. Arrange
        // Use FakeCalculator, which has real, predictable logic
        calculator = FakeCalculator()
        viewModel = CalculatorViewModel(calculator) // Inject the Fake

        // 2. Act
        viewModel.onNumberClick("5")
        viewModel.onOperatorClick("+")
        viewModel.onNumberClick("7")
        viewModel.onOperatorClick("=")

        // 3. Assert
        // Need "runBlocking" and ".first()" to get the current value from StateFlow
        val finalState = viewModel.uiState.first()

        assertEquals("12", finalState.firstOperand) // The result "12" becomes the new firstOperand
        assertEquals("", finalState.operator)
        assertEquals("12", finalState.display) // The display shows "12"
    }


    @Test
    fun `test ViewModel - using StubCalculator - simulate calculator returning 999`() = runBlocking {
        // 1. Arrange
        // Create a *specific* Stub that does only one thing: add() returns 999
        calculator = object : StubCalculator() {
            override fun add(a: Double, b: Double): Double {
                // Regardless of whether the input is 5 or 7, I always return 999
                return 999.0
            }
        }
        viewModel = CalculatorViewModel(calculator) // Inject the Stub

        // 2. Act
        viewModel.onNumberClick("5")
        viewModel.onOperatorClick("+")
        viewModel.onNumberClick("7")
        viewModel.onOperatorClick("=")

        // 3. Assert
        // Check if the ViewModel correctly *displayed* the "999" returned by the stub
        val finalState = viewModel.uiState.first()

        // This proves the ViewModel's logic (updating state) is correct,
        // even if the calculator's logic (returning 999) is "wrong".
        assertEquals("999", finalState.firstOperand)
        assertEquals("999", finalState.display)
    }

    @Test
    fun `test ViewModel - negative number feature - 5 + -2 should equal 3`() = runBlocking {
        // Use FakeCalculator again to test complex functionality

        // 1. Arrange
        calculator = FakeCalculator()
        viewModel = CalculatorViewModel(calculator)

        // 2. Act
        viewModel.onNumberClick("5")  // display: 5
        viewModel.onOperatorClick("+") // display: 5 +
        viewModel.onOperatorClick("-") // display: 5 + -
        viewModel.onNumberClick("2")  // display: 5 + -2
        viewModel.onOperatorClick("=") // display: 3

        // 3. Assert
        val finalState = viewModel.uiState.first()
        assertEquals("3", finalState.display)
    }
}