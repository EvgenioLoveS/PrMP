package com.example.ios_calculator.ThemeLogic

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object ActionThemeRepository {
    private val db = FirebaseFirestore.getInstance()

    fun saveThemeParameters(actionTheme: ActionTheme) {
        db.collection("themeSettings").document("currentTheme")
            .set(actionTheme)
            .addOnSuccessListener {
                Log.d("Firestore", "Theme parameters saved successfully")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error saving theme parameters", e)
            }
    }

    fun loadThemeParameters(onSuccess: (ActionTheme) -> Unit) {
        db.collection("themeSettings").document("currentTheme")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val actionTheme = document.toObject(ActionTheme::class.java)
                    if (actionTheme != null) {
                        onSuccess(actionTheme)
                    }
                } else {
                    Log.d("Firestore", "No such document")
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting document", e)
            }
    }
}