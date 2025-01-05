package com.example.campussafetyapp.UptoSOS

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafetyapp.R

class UptoSosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_upto_sos)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragmentContainerView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//
//        val SafetyInfoFragment = FragSafetyInfo()
//        val AcademicInfoFragment = FragAcedamicInfo()
//
//
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.fraguptosos, SafetyInfoFragment)
//            commit()
//        }


//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
//
//        val navController = navHostFragment.navController
//
//
//


    }
}