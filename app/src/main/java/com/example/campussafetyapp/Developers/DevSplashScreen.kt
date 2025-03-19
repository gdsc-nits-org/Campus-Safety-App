package com.example.campussafetyapp.Developers

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafetyapp.R

class DevSplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide Action Bar
        supportActionBar?.hide()

        // Enable Edge-to-Edge before setting content view
        enableEdgeToEdge()
        setContentView(R.layout.activity_dev_splash_screen)

        // Apply Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Delayed transition to DevelopersPage
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@DevSplashScreen, DevelopersPage::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 2 seconds delay
    }
}
