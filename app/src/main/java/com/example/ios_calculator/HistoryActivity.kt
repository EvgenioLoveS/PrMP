package com.example.ios_calculator


import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ios_calculator.HistoryLogic.ActionHistoryRepository
import com.example.ios_calculator.HistoryLogic.HistoryAdapter

class HistoryActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var actionHistoryRepository: ActionHistoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        actionHistoryRepository = ActionHistoryRepository()
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(emptyList())
        recyclerView.adapter = historyAdapter

        // Обработка нажатия кнопки "Назад"
        findViewById<Button>(R.id.buttonBack).setOnClickListener {
            finish() // Завершает HistoryActivity и возвращает к предыдущему экрану
        }

        loadActionHistory()
    }

    private fun loadActionHistory() {
        actionHistoryRepository.loadActionHistory(
            onSuccess = { history ->
                Log.d("Firestore", "Загруженная история: $history")
                historyAdapter.updateHistory(history)
            },
            onFailure = { e ->
                Log.w("Firestore", "Ошибка при загрузке истории: ", e)
            }
        )
    }
}