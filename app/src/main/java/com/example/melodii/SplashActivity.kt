package com.example.melodii

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val background = object : Thread() {
            override fun run() {
                try {
                    sleep(2000)

                    val intent = Intent(baseContext, MenuActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }

    override fun onStart() {
        super.onStart()
        logoAnim()
    }

    private fun logoAnim() {
        val logo = findViewById<ImageView>(R.id.logo)
        val logoPulse: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(logo,
            PropertyValuesHolder.ofFloat("scaleX", 1.2F),
            PropertyValuesHolder.ofFloat("scaleY", 1.2F))
        logoPulse.duration = 500
        logoPulse.repeatCount = 1
        logoPulse.repeatMode = ObjectAnimator.REVERSE
        logoPulse.interpolator = FastOutLinearInInterpolator()
        logoPulse.start()
    }
}