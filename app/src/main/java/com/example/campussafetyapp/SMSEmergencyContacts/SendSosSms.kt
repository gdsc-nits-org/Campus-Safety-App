package com.example.campussafetyapp.SMSEmergencyContacts

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.campussafetyapp.RoomDB.AppDatabase
import com.example.campussafetyapp.RoomDB.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SendSosSms(private val activity: Activity) {

    private val REQUEST_CODE_SMS = 1

    // Sends an SOS message to all emergency contacts in the database.
    fun sendEmergencySms() {
        // Check SMS permission
        if (!hasSmsPermission()) {
            requestSmsPermission()
            return
        }

        // Fetch emergency contacts and send messages
        CoroutineScope(Dispatchers.IO).launch {
            val database = AppDatabase.getDatabase(activity.applicationContext)
            val contactDao = database.contactDao()

            // Fetch all contacts from the database
            val contacts = contactDao.getAllContacts()

            if (contacts.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "No emergency contacts found.", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            // Send SMS to all contacts
            val smsManager = SmsManager.getDefault()
            val failedContacts = mutableListOf<String>() // Track failed contacts

            for (contact in contacts) {
                try {
                    // Validate phone number before sending
                    if (contact.phoneNumber.isNullOrBlank()) {
                        failedContacts.add(contact.name ?: "Unknown")
                        continue
                    }

                    smsManager.sendTextMessage(
                        contact.phoneNumber,
                        null,
                        "This is an emergency message. Google map link : ",
                        null,
                        null
                    )
                } catch (e: Exception) {
                    failedContacts.add(contact.name ?: "Unknown")
                    e.printStackTrace()
                }
            }

            // Notify the user about the status
            withContext(Dispatchers.Main) {
                if (failedContacts.isEmpty()) {
                    Toast.makeText(activity, "SOS messages sent successfully.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        activity,
                        "Failed to send messages to: ${failedContacts.joinToString(", ")}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // Checks if the app has SMS sending permissions.
    private fun hasSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Requests SMS permissions from the user.
    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.SEND_SMS),
            REQUEST_CODE_SMS
        )
    }

    // Handles the result of the SMS permission request.
    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_SMS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Permission granted. Sending SOS messages!", Toast.LENGTH_SHORT).show()
                sendEmergencySms()
            } else {
                Toast.makeText(activity, "SMS permission denied. Cannot send messages.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
