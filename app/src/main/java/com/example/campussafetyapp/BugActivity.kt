package com.example.campussafetyapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafetyapp.databinding.ActivityBugBinding

class BugActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBugBinding
    private var imageUri: Uri? = null
    // Flag to check if email intent was launched
    private var emailIntentLaunched = false

    // Register for picking an image
    @SuppressLint("SetTextI18n")
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it

            // Try to get filename from URI
            val cursor = contentResolver.query(it, null, null, null, null)
            val name = if (cursor != null && cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex("_display_name")
                val filename = if (nameIndex != -1) cursor.getString(nameIndex) else "Screenshot attached"
                cursor.close()
                filename
            } else {
                "Screenshot attached"
            }

            binding.fileName.text = name
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBugBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.chooseFileLayout.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.sendBugBtn.setOnClickListener {
            val message = binding.edittext.text.toString().trim()

            if (message.isEmpty()) {
                Toast.makeText(this, "Please describe the bug.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = if (imageUri != null) "image/*" else "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("kkunaljit@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Report a Bug")
                putExtra(Intent.EXTRA_TEXT, message)

                imageUri?.let {
                    putExtra(Intent.EXTRA_STREAM, it)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                // Force open in Gmail
                setPackage("com.google.android.gm")
            }

            try {
                emailIntentLaunched = true  // Set the flag to true when email intent is launched
                startActivity(emailIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "Gmail app not found!", Toast.LENGTH_SHORT).show()
            }

        }
    }
    override fun onResume() {
        super.onResume()
        if (emailIntentLaunched) {
            emailIntentLaunched = false
            // Navigate to DoneActivity after email intent
            startActivity(Intent(this, DoneActivity::class.java))
            finish()  // Optional: Close the current BugActivity after redirecting
        }
    }

}