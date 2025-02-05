package com.example.ios_calculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import com.example.ios_calculator.ThemeLogic.ActionThemeRepository
import com.example.ios_calculator.ThemeLogic.ActionTheme

class ThemeSettingsActivity : ComponentActivity() {

    private lateinit var etBackgroundColor: EditText
    private lateinit var etTextColor: EditText
    private lateinit var etStatusBarColor: EditText
    private lateinit var etButtonColor: EditText
    private lateinit var etButtonTextColor: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_settings)

        etBackgroundColor = findViewById(R.id.etBackgroundColor)
        etTextColor = findViewById(R.id.etTextColor)
        etStatusBarColor = findViewById(R.id.etStatusBarColor)
        etButtonColor = findViewById(R.id.etButtonColor)
        etButtonTextColor = findViewById(R.id.etButtonTextColor)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)

        // Загрузка текущих параметров темы
        ActionThemeRepository.loadThemeParameters { themeParameters ->
            etBackgroundColor.setText(themeParameters.backgroundColor)
            etTextColor.setText(themeParameters.textColor)
            etStatusBarColor.setText(themeParameters.statusBarColor)
            etButtonColor.setText(themeParameters.buttonColor) // Новый параметр
            etButtonTextColor.setText(themeParameters.buttonTextColor) // Новый параметр
        }

        // Обработчик нажатия на кнопку "Назад"
        btnBack.setOnClickListener {
            finish() // Закрыть этот экран и вернуться к предыдущему
        }

        // Обработчик нажатия на кнопку сохранения
        btnSave.setOnClickListener {
            val newParameters = ActionTheme(
                backgroundColor = etBackgroundColor.text.toString(),
                textColor = etTextColor.text.toString(),
                statusBarColor = etStatusBarColor.text.toString(),
                buttonColor = etButtonColor.text.toString(), // Новый параметр
                buttonTextColor = etButtonTextColor.text.toString() // Новый параметр
            )
            ActionThemeRepository.saveThemeParameters(newParameters)
            finish() // Закрыть этот экран после сохранения
        }
    }
}