package com.example.campussafetyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campussafetyapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        binding.signUpButton.setOnClickListener {
            val name = binding.fullNameEditText.text.toString().trim()
            val scholarID = binding.scholarIdEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val mobile = binding.phoneNumberEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            var isValid = true


            if (name.isEmpty()) {
                binding.fullNameInputLayout.error = "Full Name is required"
                isValid = false
            } else {
                binding.fullNameInputLayout.error = null
            }


            if (scholarID.isEmpty()) {
                binding.scholarIdInputLayout.error = "Scholar ID is required"
                isValid = false
            } else {
                binding.scholarIdInputLayout.error = null
            }


            if (email.isEmpty()) {
                binding.emailInputLayout.error = "Email is required"
                isValid = false
            } else if (!email.endsWith(".nits.ac.in")) {
                binding.emailInputLayout.error = "Please enter a valid institute email ID"
                isValid = false
            } else {
                binding.emailInputLayout.error = null
            }


            if (mobile.isEmpty()) {
                binding.phoneNumberInputLayout.error = "Mobile number is required"
                isValid = false
            } else {
                binding.phoneNumberInputLayout.error = null
            }


            if (password.isEmpty()) {
                binding.passwordInputLayout.error = "Password is required"
                isValid = false
            } else {
                binding.passwordInputLayout.error = null
            }


            if(isValid){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val userId = firebaseAuth.currentUser?.uid
                        if (userId != null) {

                            val user = UserData(name, scholarID, email, mobile)

                            databaseReference.child(userId).setValue(user).addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Failed to save user data: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        binding.loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}