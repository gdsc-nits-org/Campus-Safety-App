package com.example.campussafetyapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafetyapp.sos_fragments.SosSplashInFragment

class SosEmergencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sos_emergency)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.sos_fragment_container, SosSplashInFragment())
            .commit()

    }
}