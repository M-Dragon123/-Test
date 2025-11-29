package com.example.mycalculatorapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertTextEquals
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun test_5_plus_7_equals_12_on_ui() {
        composeTestRule.onNodeWithText("5").performClick()
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("7").performClick()
        composeTestRule.onNodeWithText("=").performClick()
        composeTestRule.onNodeWithTag("display").assertTextEquals("12")
    }

    @Test
    fun test_negative_numbers_ui() {
        // test：press "5" -> "+" -> "-" -> "2" -> "=" should be "3"

        composeTestRule.onNodeWithTag("display").assertTextEquals("0")

        // 1. input "5"
        composeTestRule.onNodeWithText("5").performClick()

        // 2. input "+"
        composeTestRule.onNodeWithText("+").performClick()

        // 3. input "-2"
        composeTestRule.onNodeWithText("-").performClick()
        composeTestRule.onNodeWithText("2").performClick()

        // 4. cal
        composeTestRule.onNodeWithText("=").performClick()

        // 5 + (-2) = 3
        composeTestRule.onNodeWithTag("display").assertTextEquals("3")
    }
}