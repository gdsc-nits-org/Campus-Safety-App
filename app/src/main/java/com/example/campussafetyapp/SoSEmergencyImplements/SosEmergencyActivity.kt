package com.example.campussafetyapp.SoSEmergencyImplements

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.campussafetyapp.R

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