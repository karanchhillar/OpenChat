package com.example.openchat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.openchat.R
import com.example.openchat.login.LoginPage

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 1000 // 1 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent = Intent(this, LoginPage::class.java)
//        startActivity(intent)

        Handler().postDelayed({
            // Start the next activity
//            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)

            // Finish the current activity
            finish()
        }, SPLASH_DELAY)
    }
}