package com.example.ios_calculator

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ios_calculator.API.GestureHandler
import com.example.ios_calculator.API.HapticFeedbackHelper
import com.example.ios_calculator.logic.CalculatorLogic
import com.example.ios_calculator.HistoryLogic.ActionHistoryRepository
import com.example.ios_calculator.ThemeLogic.ActionThemeRepository
import com.example.ios_calculator.ThemeLogic.ActionTheme

class MainActivity : ComponentActivity() {

    private lateinit var tvDisplay: TextView
    private lateinit var calculatorLogic: CalculatorLogic
    private lateinit var gestureDetector: GestureDetector
    private lateinit var hapticFeedbackHelper: HapticFeedbackHelper
    private lateinit var actionHistoryRepository: ActionHistoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        actionHistoryRepository = ActionHistoryRepository()
        calculatorLogic = CalculatorLogic(actionHistoryRepository)
        tvDisplay = findViewById(R.id.tvDisplay)
        hapticFeedbackHelper = HapticFeedbackHelper(this) // Инициализация вибратора

        // Загрузка текущих параметров темы
        ActionThemeRepository.loadThemeParameters { themeParameters ->
            applyTheme(themeParameters)
        }

        // Инициализация кнопки для перехода к настройкам темы
        findViewById<Button>(R.id.btnOpenThemeSettings).setOnClickListener {
            val intent = Intent(this, ThemeSettingsActivity::class.java)
            startActivity(intent)
        }

        // Установка обработчика нажатий на кнопку истории
        findViewById<Button>(R.id.btnHistory).setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        // Инициализация жестов
        gestureDetector = GestureDetector(this, GestureHandler(
            onSwipeLeft = { onDeleteLast() },
            onSwipeDown = { onClearClick() }
        ))

        // Устанавливаем обработчик касаний на экран вывода
        tvDisplay.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            // Если событие - это нажатие, вызываем performClick
            if (event.action == MotionEvent.ACTION_UP) {
                tvDisplay.performClick()
            }
            true // Возвращаем true, чтобы указать, что событие обработано
        }

        setupButtons()
    }

    private fun setupButtons() {
        val buttons = listOf(
            findViewById<Button>(R.id.btnAC),
            findViewById<Button>(R.id.btnEntry),
            findViewById<Button>(R.id.btnPlusMinus),
            findViewById<Button>(R.id.btnPercent),
            findViewById<Button>(R.id.btnDivide),
            findViewById<Button>(R.id.btnMultiply),
            findViewById<Button>(R.id.btnMinus),
            findViewById<Button>(R.id.btnPlus),
            findViewById<Button>(R.id.btnEquals),
            findViewById<Button>(R.id.btnDot),
            findViewById<Button>(R.id.btn0),
            findViewById<Button>(R.id.btn1),
            findViewById<Button>(R.id.btn2),
            findViewById<Button>(R.id.btn3),
            findViewById<Button>(R.id.btn4),
            findViewById<Button>(R.id.btn5),
            findViewById<Button>(R.id.btn6),
            findViewById<Button>(R.id.btn7),
            findViewById<Button>(R.id.btn8),
            findViewById<Button>(R.id.btn9),
            findViewById<Button>(R.id.btnSin),
            findViewById<Button>(R.id.btnCos),
            findViewById<Button>(R.id.btnTan),
            findViewById<Button>(R.id.btnCtg),
            findViewById<Button>(R.id.btnSqrt)
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                hapticFeedbackHelper.vibrate() // Виброотклик при нажатии кнопки
                onButtonClick(button)
            }
        }
    }

    private fun onButtonClick(button: Button) {
        when (button.id) {
            R.id.btnAC -> onClearClick()
            R.id.btnEntry -> onDeleteLast()
            R.id.btnPlusMinus -> onPlusMinusClick()
            R.id.btnPercent -> onPercentClick()
            R.id.btnDivide -> onOperatorClick("/")
            R.id.btnMultiply -> onOperatorClick("×")
            R.id.btnMinus -> onOperatorClick("-")
            R.id.btnPlus -> onOperatorClick("+")
            R.id.btnEquals -> onEqualsClick()
            R.id.btnDot -> onDotClick()
            R.id.btnSin -> onTrigonometricClick("sin")
            R.id.btnCos -> onTrigonometricClick("cos")
            R.id.btnTan -> onTrigonometricClick("tg")
            R.id.btnCtg -> onTrigonometricClick("ctg")
            R.id.btnSqrt -> onSqrtClick()
            else -> {
                val number = button.text.toString()
                onNumberClick(number)
            }
        }
    }

    private fun onNumberClick(number: String) {
        calculatorLogic.currentInput = if (calculatorLogic.currentInput == "0") {
            number
        } else {
            calculatorLogic.currentInput + number
        }
        tvDisplay.text = calculatorLogic.currentInput
    }

    private fun onOperatorClick(op: String) {
        if (calculatorLogic.operator != null) {
            tvDisplay.text = calculatorLogic.calculateResult()
        }

        calculatorLogic.previousInput = calculatorLogic.currentInput
        calculatorLogic.operator = op
        calculatorLogic.currentInput = "0"
    }

    private fun onEqualsClick() {
        val num1 = calculatorLogic.previousInput
        val op = calculatorLogic.operator
        val num2 = calculatorLogic.currentInput

        val result = calculatorLogic.calculateResult()
        tvDisplay.text = result

        // Сохраняем корректное выражение в Firestore
        if (num1.isNotEmpty() && op != null) {
            actionHistoryRepository.saveActionHistory("$num1 $op $num2 = $result")
        }
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

    private fun applyTheme(actionTheme: ActionTheme) {
        // Применение цвета фона
        findViewById<LinearLayout>(R.id.rootLayout).setBackgroundColor(Color.parseColor(actionTheme.backgroundColor))

        // Применение цвета текста
        tvDisplay.setTextColor(Color.parseColor(actionTheme.textColor))

        // Применение цвета статус-бара
//        window.statusBarColor = Color.parseColor(actionTheme.statusBarColor)

        // Применение цвета статус-бара
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(actionTheme.statusBarColor)
        }

        // Применение цвета кнопок
        val buttons = listOf(
            findViewById<Button>(R.id.btnAC),
            findViewById<Button>(R.id.btnEntry),
            findViewById<Button>(R.id.btnPlusMinus),
            findViewById<Button>(R.id.btnPercent),
            findViewById<Button>(R.id.btnDivide),
            findViewById<Button>(R.id.btnMultiply),
            findViewById<Button>(R.id.btnMinus),
            findViewById<Button>(R.id.btnPlus),
            findViewById<Button>(R.id.btnEquals),
            findViewById<Button>(R.id.btnDot),
            findViewById<Button>(R.id.btn0),
            findViewById<Button>(R.id.btn1),
            findViewById<Button>(R.id.btn2),
            findViewById<Button>(R.id.btn3),
            findViewById<Button>(R.id.btn4),
            findViewById<Button>(R.id.btn5),
            findViewById<Button>(R.id.btn6),
            findViewById<Button>(R.id.btn7),
            findViewById<Button>(R.id.btn8),
            findViewById<Button>(R.id.btn9),
            findViewById<Button>(R.id.btnSin),
            findViewById<Button>(R.id.btnCos),
            findViewById<Button>(R.id.btnTan),
            findViewById<Button>(R.id.btnCtg),
            findViewById<Button>(R.id.btnSqrt),
            findViewById<Button>(R.id.btnOpenThemeSettings),
            findViewById<Button>(R.id.btnHistory)
        )

        buttons.forEach { button ->
            button.setBackgroundColor(Color.parseColor(actionTheme.buttonColor)) // Цвет кнопок
            button.setTextColor(Color.parseColor(actionTheme.buttonTextColor)) // Цвет текста в кнопках
        }
    }

}