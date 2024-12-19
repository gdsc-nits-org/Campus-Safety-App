package com.example.campussafetyapp
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.campussafetyapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        binding.btnEmergencyContacts.setOnClickListener {
            Log.d("MainActivity", "Emergency Contacts Button Clicked")
            val intent = Intent(this, EmergencyContactsActivity::class.java)
            startActivity(intent)
        }
    }
}
