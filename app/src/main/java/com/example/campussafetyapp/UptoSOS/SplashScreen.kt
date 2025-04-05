package com.example.campussafetyapp.UptoSOS

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafetyapp.HomePageWork.HomePageMain
import com.example.campussafetyapp.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        auth = FirebaseAuth.getInstance()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).postDelayed({

            val currentUser = auth.currentUser
            if (currentUser != null) {
                // User already logged in → Go to HomePage
                startActivity(Intent(this, HomePageMain::class.java))
            } else {
                // User not logged in → Go to Login/UptoSosActivity
                startActivity(Intent(this, UptoSosActivity::class.java))
            }

            finish()

        }, 3000)
    }
}