package com.example.ios_calculator.HistoryLogic

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActionHistoryRepository {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val docRef = firestore.collection("actionHistory").document("userHistory")

    fun saveActionHistory(action: String) {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    docRef.update("history", com.google.firebase.firestore.FieldValue.arrayUnion(action))
                        .addOnSuccessListener {
                            Log.d("Firestore", "История действий успешно обновлена!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Ошибка при обновлении истории: ", e)
                        }
                } else {
                    val newHistory = ActionHistory(history = listOf(action))
                    docRef.set(newHistory)
                        .addOnSuccessListener {
                            Log.d("Firestore", "История действий успешно сохранена!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Ошибка при сохранении истории: ", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Ошибка при проверке существования документа: ", e)
            }
    }

    fun loadActionHistory(onSuccess: (List<String>) -> Unit, onFailure: (Exception) -> Unit) {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val actionHistory = document.toObject(ActionHistory::class.java)
                    actionHistory?.let {
                        Log.d("Firestore", "Загруженная история: ${it.history}")
                        onSuccess(it.history)
                    } ?: onSuccess(emptyList())
                } else {
                    Log.d("Firestore", "Документ не найден")
                    onSuccess(emptyList())
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Ошибка при загрузке истории: ", e)
                onFailure(e)
            }
    }
}