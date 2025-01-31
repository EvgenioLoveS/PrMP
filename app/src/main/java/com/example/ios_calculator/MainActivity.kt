package com.example.ios_calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ios_calculator.logic.CalculatorLogic

class MainActivity : ComponentActivity() {

    private lateinit var tvDisplay: TextView
    private val calculatorLogic = CalculatorLogic()

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
        val btnSin: Button = findViewById(R.id.btnSin)
        val btnCos: Button = findViewById(R.id.btnCos)
        val btnTan: Button = findViewById(R.id.btnTan)
        val btnCtg: Button = findViewById(R.id.btnCtg)
        val btnSqrt: Button = findViewById(R.id.btnSqrt)

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

        // Обработчики тригонометрических функций
        btnSin.setOnClickListener { onTrigonometricClick("sin") }
        btnCos.setOnClickListener { onTrigonometricClick("cos") }
        btnTan.setOnClickListener { onTrigonometricClick("tg") }
        btnCtg.setOnClickListener { onTrigonometricClick("ctg") }
        btnSqrt.setOnClickListener { onSqrtClick() }
    }

    private fun onNumberClick(button: Button) {
        calculatorLogic.currentInput = if (calculatorLogic.currentInput == "0") {
            button.text.toString()
        } else {
            calculatorLogic.currentInput + button.text.toString()
        }
        tvDisplay.text = calculatorLogic.currentInput
    }


    private fun onOperatorClick(op: String) {
        if (calculatorLogic.operator != null) {
            // Если оператор уже был введен, пересчитываем результат перед новым оператором
            tvDisplay.text = calculatorLogic.calculateResult()
        }

        calculatorLogic.previousInput = calculatorLogic.currentInput
        calculatorLogic.operator = op
        calculatorLogic.currentInput = "0"
    }



    private fun onEqualsClick() {
        tvDisplay.text = calculatorLogic.calculateResult()
    }

    private fun onClearClick() {
        calculatorLogic.clear()
        tvDisplay.text = calculatorLogic.currentInput
    }

    private fun onDeleteLast() {
        calculatorLogic.deleteLast()
        tvDisplay.text = calculatorLogic.currentInput
    }

    private fun onPlusMinusClick() {
        calculatorLogic.onPlusMinusClick()
        tvDisplay.text = calculatorLogic.currentInput
    }

    private fun onPercentClick() {
        calculatorLogic.onPercentClick()
        tvDisplay.text = calculatorLogic.currentInput
    }

    private fun onDotClick() {
        // Логика для добавления точки
        if (!calculatorLogic.currentInput.contains(".")) {
            calculatorLogic.currentInput += "."
            tvDisplay.text = calculatorLogic.currentInput
        }
    }

    private fun onTrigonometricClick(func: String) {
        tvDisplay.text = calculatorLogic.onTrigonometricClick(func)
    }

    private fun onSqrtClick() {
        calculatorLogic.onSqrtClick()
        tvDisplay.text = calculatorLogic.currentInput
    }
}