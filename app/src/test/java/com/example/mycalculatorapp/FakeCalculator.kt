package com.example.mycalculatorapp

import kotlin.math.sqrt

/**
 * FakeCalculator (伪对象)
 *
 * 这是一个功能齐全的计算器，用于测试环境。
 * 它的逻辑和 RealCalculator 一样，但在测试时我们可以完全控制它。
 */
class FakeCalculator : ICalculator {
    private var internalState = 0.0

    override fun add(a: Double, b: Double): Double {
        internalState = a + b
        return internalState
    }

    override fun subtract(a: Double, b: Double): Double {
        internalState = a - b
        return internalState
    }

    override fun multiply(a: Double, b: Double): Double {
        internalState = a * b
        return internalState
    }

    override fun divide(a: Double, b: Double): Double {
        if (b == 0.0) {
            internalState = Double.NaN
            return internalState
        }
        internalState = a / b
        return internalState
    }

    override fun sqrt(a: Double): Double {
        if (a < 0.0) {
            internalState = Double.NaN
            return internalState
        }
        internalState = sqrt(a)
        return internalState
    }

    override fun percentage(a: Double): Double {
        internalState = a / 100.0
        return internalState
    }

    override fun clear() {
        internalState = 0.0
    }
}