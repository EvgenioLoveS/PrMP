package com.example.ios_calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlin.math.*

class MainActivity : ComponentActivity() {

    private lateinit var tvDisplay: TextView
    private var currentInput: String = "0"
    private var previousInput: String = ""
    private var operator: String? = null
    private var isOperatorClicked: Boolean = false
    private var isEqualsClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        tvDisplay = findViewById(R.id.tvDisplay)

        val btnAC: Button = findViewById(R.id.btnAC)
        val btnEntry: Button = findViewById(R.id.btnEntry)
        val btnPlusMinus: Button = findViewById(R.id.btnPlusMinus)
        val btnPercent: Button = findViewById(R.id.btnPercent)
        val btnDivide: Button = findViewById(R.id.btnDivide)
        val btnMultiply: Button = findViewById(R.id.btnMultiply)
        val btnMinus: Button = findViewById(R.id.btnMinus)
        val btnPlus: Button = findViewById(R.id.btnPlus)
        val btnEquals: Button = findViewById(R.id.btnEquals)
        val btnDot: Button = findViewById(R.id.btnDot)
        val btn0: Button = findViewById(R.id.btn0)
        val btn1: Button = findViewById(R.id.btn1)
        val btn2: Button = findViewById(R.id.btn2)
        val btn3: Button = findViewById(R.id.btn3)
        val btn4: Button = findViewById(R.id.btn4)
        val btn5: Button = findViewById(R.id.btn5)
        val btn6: Button = findViewById(R.id.btn6)
        val btn7: Button = findViewById(R.id.btn7)
        val btn8: Button = findViewById(R.id.btn8)
        val btn9: Button = findViewById(R.id.btn9)

        val buttons = arrayOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
        buttons.forEach { button ->
            button.setOnClickListener { onNumberClick(button) }
        }

        btnAC.setOnClickListener { onClearClick() }
        btnEntry.setOnClickListener { onDeleteLast() }
        btnPlusMinus.setOnClickListener { onPlusMinusClick() }
        btnPercent.setOnClickListener { onPercentClick() }
        btnDivide.setOnClickListener { onOperatorClick("/") }
        btnMultiply.setOnClickListener { onOperatorClick("×") }
        btnMinus.setOnClickListener { onOperatorClick("-") }
        btnPlus.setOnClickListener { onOperatorClick("+") }
        btnEquals.setOnClickListener { onEqualsClick() }
        btnDot.setOnClickListener { onDotClick() }
    }

    private fun onNumberClick(button: Button) {
        if (isEqualsClicked) {
            currentInput = button.text.toString()
            isEqualsClicked = false
        } else {
            if (currentInput.length < 9) {
                currentInput = if (currentInput == "0" || isOperatorClicked) {
                    button.text.toString()
                } else {
                    currentInput + button.text.toString()
                }
            }
        }
        tvDisplay.text = currentInput
        isOperatorClicked = false
    }

    private fun onOperatorClick(op: String) {
        if (isOperatorClicked) {
            operator = op
        } else {
            if (operator != null) {
                calculateResult()
            }
            previousInput = currentInput
            operator = op
            isOperatorClicked = true
        }
    }

    private fun onEqualsClick() {
        calculateResult()
        operator = null
        isEqualsClicked = true
    }

    private fun calculateResult() {
        val num1 = previousInput.toDoubleOrNull()
        val num2 = currentInput.toDoubleOrNull()

        if (num1 != null && num2 != null) {
            val result = when (operator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "×" -> num1 * num2
                "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                else -> null
            }
            if (result != null && !result.isNaN()) {
                currentInput = result.toString()
                tvDisplay.text = currentInput
            } else {
                tvDisplay.text = "Error"
                currentInput = "0"
            }
        }
    }

    private fun onClearClick() {
        currentInput = "0"
        previousInput = ""
        operator = null
        tvDisplay.text = currentInput
    }

    private fun onDeleteLast() {
        if (currentInput.length > 1) {
            currentInput = currentInput.dropLast(1)
        } else {
            currentInput = "0"
        }
        tvDisplay.text = currentInput
    }

    private fun onPlusMinusClick() {
        if (currentInput != "0") {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)
            } else {
                "-$currentInput"
            }
            tvDisplay.text = currentInput
        }
    }

    private fun onPercentClick() {
        currentInput = (currentInput.toDoubleOrNull()?.div(100)?.toString()) ?: "Error"
        tvDisplay.text = currentInput
    }

    private fun onDotClick() {
        if (!currentInput.contains(".")) {
            currentInput += "."
            tvDisplay.text = currentInput
        }
    }
}
