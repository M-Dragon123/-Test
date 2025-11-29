package com.example.mycalculatorapp

/**
 * StubCalculator (桩)
 *
 * 一个通用的桩实现，所有函数都返回默认值 (0.0) 或什么都不做。
 * 在测试中，我们通常会创建一个*特定*的桩。
 */
open class StubCalculator : ICalculator {
    override fun add(a: Double, b: Double): Double = 0.0
    override fun subtract(a: Double, b: Double): Double = 0.0
    override fun multiply(a: Double, b: Double): Double = 0.0
    override fun divide(a: Double, b: Double): Double = 0.0
    override fun sqrt(a: Double): Double = 0.0
    override fun percentage(a: Double): Double = 0.0
    override fun clear() { }
}