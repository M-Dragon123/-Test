package com.example.mycalculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// 👇👇👇 【修改 1：添加这个 import】 👇👇👇
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycalculatorapp.ui.theme.MyCalculatorAppTheme

class MainActivity : ComponentActivity() {

    // 通过 "by viewModels()" 委托来获取 ViewModel 实例
    // Android 系统会自动管理这个 ViewModel 的生命周期
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCalculatorAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 将 ViewModel 传递给您的屏幕
                    CalculatorScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {

    // 从 ViewModel "收集" UI 状态
    // 当状态改变时，这个 Composable 会自动重组 (刷新)
    val uiState by viewModel.uiState.collectAsState()

    val buttonRows = listOf(
        listOf("C", "sqrt", "%", "/"),
        listOf("7", "8", "9", "*"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom // 将所有内容推到底部
    ) {

        // 1. 显示屏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 32.dp),
            contentAlignment = Alignment.CenterEnd // 文本靠右对齐
        ) {
            Text(
                text = uiState.display,
                fontSize = 64.sp,
                maxLines = 1,
                // 👇👇👇 【修改 2：添加 testTag，让测试能找到它】 👇👇👇
                modifier = Modifier.testTag("display")
            )
        }

        // 2. 按钮网格
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp) // 行之间的垂直间距
        ) {
            buttonRows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // 按钮之间的水平间距
                ) {
                    row.forEach { buttonText ->
                        CalculatorButton(
                            text = buttonText,
                            onClick = { onButtonClick(viewModel, buttonText) },
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f) // 保持按钮为 1:1 正方形
                        )
                    }
                }
            }

            // 3. 渲染最后一行 (0, ., =)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // "0" 按钮 (占 2 个空间)
                CalculatorButton(
                    text = "0",
                    onClick = { onButtonClick(viewModel, "0") },
                    modifier = Modifier
                        .weight(2.05f) // 权重为2，加上间距的0.05
                        .aspectRatio(2f / 1f) // 比例为 2:1
                )
                // "." 按钮
                CalculatorButton(
                    text = ".",
                    onClick = { onButtonClick(viewModel, ".") },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
                // "=" 按钮
                CalculatorButton(
                    text = "=",
                    onClick = { onButtonClick(viewModel, "=") },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
        }
    }
}

/**
 * 助手函数，用于将 UI 点击路由到正确的 ViewModel 函数
 */
private fun onButtonClick(viewModel: CalculatorViewModel, buttonText: String) {
    when (buttonText) {
        "C" -> viewModel.onOperatorClick(buttonText)
        "=", "+", "-", "*", "/" -> viewModel.onOperatorClick(buttonText)

        "." -> viewModel.onDecimalClick()
        "%" -> viewModel.onPercentageClick()
        "sqrt" -> viewModel.onSqrtClick()

        // 其他情况（所有数字）
        else -> viewModel.onNumberClick(buttonText)
    }
}


/**
 * 这是一个可重用的按钮 Composable
 */
@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text, fontSize = 24.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    MyCalculatorAppTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "计算器显示: 0", fontSize = 32.sp)
        }
    }
}