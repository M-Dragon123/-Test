package com.example.mycalculatorapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat

data class CalculatorUiState(
    val firstOperand: String = "",
    val operator: String = "",
    val secondOperand: String = ""
) {
    /**
     * "display" 是一个计算属性。
     * UI 总是根据当前状态显示正确的内容。
     */
    val display: String
        get() = when {
            // 案例 1: 正在输入第二个数 (例如: "123 + 456")
            secondOperand.isNotEmpty() -> "$firstOperand $operator $secondOperand"
            // 案例 2: 刚刚选择操作符 (例如: "123 +")
            operator.isNotEmpty() -> "$firstOperand $operator"
            // 案例 3: 正在输入第一个数 (例如: "123")
            firstOperand.isNotEmpty() -> firstOperand
            // 案例 4: 初始状态
            else -> "0"
        }
}

class CalculatorViewModel(
    private val calculator: ICalculator = RealCalculator()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    /**
     * 当数字按钮被点击
     */
    fun onNumberClick(number: String) {
        _uiState.update { state ->
            if (state.operator.isNotEmpty()) {
                state.copy(secondOperand = state.secondOperand + number)
            } else {
                val newFirst = if (state.firstOperand == "0") number else state.firstOperand + number
                state.copy(firstOperand = newFirst)
            }
        }
    }

    /**
     * 当操作符按钮被点击
     */
    fun onOperatorClick(op: String) {
        when (op) {
            "C" -> performClear()
            "=" -> performEquals()
            else -> performOperation(op) // 用于 "+", "-", "*", "/"
        }
    }

    /**
     * 当小数点 "." 按钮被点击
     */
    fun onDecimalClick() {
        _uiState.update { state ->
            // 案例 1: 正在输入第二个数
            if (state.operator.isNotEmpty()) {
                if (!state.secondOperand.contains(".")) {
                    val newSecond = when {
                        state.secondOperand.isEmpty() -> "0."
                        state.secondOperand == "-" -> "-0." // e.g., "5 + -" -> "5 + -0."
                        else -> state.secondOperand + "."
                    }
                    state.copy(secondOperand = newSecond)
                } else {
                    state
                }
            }
            // 案例 2: 正在输入第一个数
            else {
                if (!state.firstOperand.contains(".")) {
                    val newFirst = when {
                        state.firstOperand.isEmpty() -> "0."
                        state.firstOperand == "-" -> "-0." // e.g., "-" -> "-0."
                        else -> state.firstOperand + "."
                    }
                    state.copy(firstOperand = newFirst)
                } else {
                    state
                }
            }
        }
    }

    /**
     * 当百分比 "%" 按钮被点击
     */
    fun onPercentageClick() {
        _uiState.update { state ->
            // 案例 1: 正在输入第二个数
            if (state.secondOperand.isNotEmpty() && state.secondOperand != "-") {
                val a = state.secondOperand.toDoubleOrNull() ?: 0.0
                val result = calculator.percentage(a)
                state.copy(secondOperand = formatResult(result))
            }
            // 案例 2: 正在输入第一个数 (且没有操作符)
            else if (state.firstOperand.isNotEmpty() && state.firstOperand != "-" && state.operator.isEmpty()) {
                val a = state.firstOperand.toDoubleOrNull() ?: 0.0
                val result = calculator.percentage(a)
                state.copy(firstOperand = formatResult(result))
            }
            // 案例 3: 其他情况 (例如只有 "5 +")，什么都不做
            else {
                state
            }
        }
    }

    /**
     * 当平方根 "sqrt" 按钮被点击
     */
    fun onSqrtClick() {
        _uiState.update { state ->
            // 案例 1: 正在输入第二个数
            if (state.secondOperand.isNotEmpty() && state.secondOperand != "-") {
                val a = state.secondOperand.toDoubleOrNull() ?: 0.0
                val result = calculator.sqrt(a) // 调用接口的 sqrt
                state.copy(secondOperand = formatResult(result))
            }
            // 案例 2: 正在输入第一个数 (且没有操作符)
            else if (state.firstOperand.isNotEmpty() && state.firstOperand != "-" && state.operator.isEmpty()) {
                val a = state.firstOperand.toDoubleOrNull() ?: 0.0
                val result = calculator.sqrt(a)
                state.copy(firstOperand = formatResult(result))
            }
            // 案例 3: 其他情况 (例如 "123 +")，什么都不做
            else {
                state
            }
        }
    }

    private fun performClear() {
        calculator.clear()
        _uiState.value = CalculatorUiState()
    }

    /**
     * 执行操作 (e.g., "+", "-", "*", "/")
     */
    private fun performOperation(op: String) {
        _uiState.update { state ->

            // 案例 1: "1 + 2" 之后按 "-" (连锁计算)
            if (state.firstOperand.isNotEmpty() && state.firstOperand != "-" &&
                state.secondOperand.isNotEmpty() && state.secondOperand != "-") {
                val result = performCalculation()
                state.copy(
                    firstOperand = formatResult(result),
                    operator = op,
                    secondOperand = ""
                )
            }
            // 案例 2: 设置操作符 (e.g., "5 +")
            else if (state.firstOperand.isNotEmpty() && state.firstOperand != "-" && state.operator.isEmpty()) {
                state.copy(operator = op)
            }
            // 案例 3: 输入负号 (e.g., "5 + -")
            else if (state.firstOperand.isNotEmpty() && state.firstOperand != "-" && state.operator.isNotEmpty() && state.secondOperand.isEmpty()) {
                if (op == "-") {
                    state.copy(secondOperand = "-")
                } else {
                    state // 不允许 "5 + *"
                }
            }
            // 案例 4: 输入初始负号 (e.g., "-")
            else if (op == "-" && state.firstOperand.isEmpty() && state.operator.isEmpty()) {
                state.copy(firstOperand = "-")
            }
            // 案例 5: 其他情况
            else {
                state
            }
        }
    }

    /**
     * 执行等于
     */
    private fun performEquals() {
        _uiState.update { state ->
            // 必须有全部三个部分才能计算, 且操作数不能只是 "-"
            if (state.firstOperand.isNotEmpty() && state.firstOperand != "-" &&
                state.operator.isNotEmpty() &&
                state.secondOperand.isNotEmpty() && state.secondOperand != "-") {

                val result = performCalculation()
                // 计算完成，结果成为新的第一个数，并清除操作
                state.copy(
                    firstOperand = formatResult(result),
                    operator = "",
                    secondOperand = ""
                )
            } else {
                // 缺少部分，无法计算
                state
            }
        }
    }

    /**
     * 助手函数：调用 ICalculator 来执行计算
     */
    private fun performCalculation(): Double {
        val state = _uiState.value
        val a = state.firstOperand.toDoubleOrNull() ?: 0.0
        val b = state.secondOperand.toDoubleOrNull() ?: 0.0

        return when (state.operator) {
            "+" -> calculator.add(a, b)
            "-" -> calculator.subtract(a, b)
            "*" -> calculator.multiply(a, b)
            "/" -> calculator.divide(a, b)
            else -> 0.0
        }
    }

    /**
     * 助手函数：格式化结果 (例如，将 10.0 显示为 "10")
     */
    private fun formatResult(result: Double): String {
        // sqrt(-1) 会返回 NaN，这个检查可以捕获它
        if (result.isNaN() || result.isInfinite()) {
            return "Error"
        }
        val formatter = DecimalFormat("0.##############")
        return formatter.format(result)
    }
}