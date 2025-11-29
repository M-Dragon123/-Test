package com.example.mycalculatorapp;

/**
 * 定义计算器所有功能的接口。
 * 这是我们的 "合约"，所有真实的、伪造的或桩实现都必须遵守。
 */
public interface ICalculator {

    // 1. 加法
    double add(double a, double b);

    // 2. 减法
    double subtract(double a, double b);

    // 3. 乘法
    double multiply(double a, double b);

    // 4. 除法
    double divide(double a, double b);

    // 5. 平方根
    double sqrt(double a);

    // 6. 百分比 (例如 50 -> 0.5)
    double percentage(double a);

    // 7. 清除 (重置状态)
    void clear();
}