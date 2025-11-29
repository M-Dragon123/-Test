package com.example.mycalculatorapp

class DummyCalculator : ICalculator {
    override fun add(a: Double, b: Double): Double {
        // 如果这个函数被调用，测试就应该失败！
        throw UnsupportedOperationException("Dummy 'add' was called!")
    }

    override fun subtract(a: Double, b: Double): Double {
        TODO("Not yet implemented")
    }

    override fun multiply(a: Double, b: Double): Double {
        TODO("Not yet implemented")
    }

    override fun divide(a: Double, b: Double): Double {
        TODO("Not yet implemented")
    }

    override fun sqrt(a: Double): Double {
        TODO("Not yet implemented")
    }

    override fun percentage(a: Double): Double {
        TODO("Not yet implemented")
    }

    // ... (所有其他函数也都抛出异常)
    override fun clear() {
        throw UnsupportedOperationException("Dummy 'clear' was called!")
    }
}