package com.example.campussafetyapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafetyapp.databinding.ActivityDoneBinding

class DoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Optional: If you have a back button in your layout
        binding.back.setOnClickListener {
            navigateBackToBugActivity()
        }
    }

    // Override system back button
    override fun onBackPressed() {
        super.onBackPressed()
        navigateBackToBugActivity()
    }

    private fun navigateBackToBugActivity() {
        val intent = Intent(this, BugActivity::class.java)
        startActivity(intent)
        finish()
    }
}
