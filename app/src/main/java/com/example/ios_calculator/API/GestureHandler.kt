package com.example.ios_calculator.API

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class GestureHandler(
    private val onSwipeLeft: () -> Unit,
    private val onSwipeDown: () -> Unit
) : GestureDetector.SimpleOnGestureListener() {

    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1 != null && e2 != null) {
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y

            return when {
                // Свайп влево (удаление последнего символа)
                diffX < -SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD -> {
                    onSwipeLeft()
                    true
                }

                // Свайп вниз (очистка экрана)
                diffY > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD -> {
                    onSwipeDown()
                    true
                }

                else -> false
            }
        }
        return false
    }
}