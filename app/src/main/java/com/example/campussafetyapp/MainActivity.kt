package com.example.campussafetyapp
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.campussafetyapp.EmergencyContacts.EmergencyContactsActivity
import com.example.campussafetyapp.SMSEmergencyContacts.SendSosSms
import com.example.campussafetyapp.databinding.ActivityMainBinding
import com.example.campussafetyapp.R



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sendSosSmsHelper: SendSosSms

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