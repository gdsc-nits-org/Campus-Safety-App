package com.example.campussafetyapp.AcademicHomePage

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafetyapp.R
import com.example.campussafetyapp.TaskManager.TaskManagerActivity

class AcademicHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_academic_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val taskPlanner = findViewById<ImageView>(R.id.taskPlanner)

        taskPlanner.setOnClickListener {
            val intent = Intent(this, TaskManagerActivity::class.java)
            startActivity(intent)
        }


    }
}