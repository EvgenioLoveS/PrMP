package com.example.ios_calculator.logic

import kotlin.math.*
import com.example.ios_calculator.HistoryLogic.ActionHistoryRepository

class CalculatorLogic(private val actionHistoryRepository: ActionHistoryRepository) {

    var currentInput: String = "0"
    var previousInput: String = ""
    var operator: String? = null


    fun calculateResult(): String {
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
            return if (result != null && !result.isNaN()) {
                currentInput = result.toString()
                previousInput = currentInput // Сохраняем результат для следующих операций
                operator = null
                currentInput
            } else {
                "Error"
            }
        }
        return "Error"
    }


    fun onPlusMinusClick() {
        if (currentInput != "0") {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)
            } else {
                "-$currentInput"
            }
        }
    }


    fun onPercentClick() {
        val inputValue = currentInput.toDoubleOrNull()
        if (inputValue != null) {
            previousInput = currentInput // Сохраняем текущее значение
            currentInput = (inputValue / 100).toString() // Вычисляем процент
            actionHistoryRepository.saveActionHistory("Percent of $previousInput = $currentInput")
        } else {
            currentInput = "Error"
        }
    }

    fun onSqrtClick() {
        val inputValue = currentInput.toDoubleOrNull()
        if (inputValue != null) {
            previousInput = currentInput // Сохраняем текущее значение
            currentInput = sqrt(inputValue).toString() // Вычисляем корень
            actionHistoryRepository.saveActionHistory("Sqrt of $previousInput = $currentInput")
        } else {
            currentInput = "Error"
        }
    }

    fun onTrigonometricClick(func: String): String {
        val input = currentInput.toDoubleOrNull()
        return if (input != null) {
            val result = when (func) {
                "sin" -> sin(Math.toRadians(input))
                "cos" -> cos(Math.toRadians(input))
                "tg" -> tan(Math.toRadians(input))
                "ctg" -> if (tan(Math.toRadians(input)) != 0.0) 1 / tan(Math.toRadians(input)) else Double.NaN
                else -> null
            }
            if (result != null && !result.isNaN()) {
                currentInput = result.toString()
                actionHistoryRepository.saveActionHistory("$func($input) = $currentInput")
                currentInput
            } else {
                "Error"
            }
        } else {
            "Error"
        }
    }

    fun clear() {
        currentInput = "0"
        previousInput = ""
        operator = null
    }

    fun deleteLast() {
        if (currentInput.length > 1) {
            currentInput = currentInput.dropLast(1)
        } else {
            currentInput = "0"
        }
    }
}