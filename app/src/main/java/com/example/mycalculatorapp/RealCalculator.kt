package com.example.mycalculatorapp

import kotlin.math.sqrt

// 正确! 明确声明 RealCalculator 实现了 ICalculator 接口
class RealCalculator : ICalculator {

    private var internalState = 0.0

    // 并且，所有这些函数都必须有 "override" 关键字
    // "override" 关键字是您对 Kotlin 保证："我正在实现接口中的这个函数"
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
        if (a < 0) {
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
        println("RealCalculator: State cleared.")
    }
}