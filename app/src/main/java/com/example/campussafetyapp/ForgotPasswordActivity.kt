package com.example.campussafetyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campussafetyapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.sendResetLinkButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailInputLayout.error = "Email field can't be empty"
            } else {
                binding.emailInputLayout.error = null
                resetPassword(email)
            }
        }
    }

    private fun resetPassword(email: String) {
        binding.sendResetLinkButton.isEnabled = false

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Reset Password link has been sent to your Institute Email-ID", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                binding.sendResetLinkButton.isEnabled = true
            }
        }
    }
}