package com.example.campussafetyapp.SMSEmergencyContacts

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.campussafetyapp.RoomDB.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SendSosSms(private val activity: Activity) {

    private val REQUEST_CODE_SMS = 1
    private val selectedContacts = mutableListOf<Contact>()



     // Sends an SOS message to all contacts in the selectedContacts list.

    fun sendEmergencySms() {
        // Check SMS permission
        if (!hasSmsPermission()) {
            requestSmsPermission()
            return
        }

        // If emergency contact is empty
        if (selectedContacts.isEmpty()) {
            Toast.makeText(activity, "No emergency contacts ", Toast.LENGTH_SHORT).show()
            return
        }

        // Send SMS to all emergency contacts
        CoroutineScope(Dispatchers.IO).launch {
            val smsManager = SmsManager.getDefault()
            for (contact in selectedContacts) {
                try {
                    smsManager.sendTextMessage(
                        contact.phoneNumber,
                        null,
                        "This is an emergency message. Please respond quickly.",
                        null,
                        null
                    )
                    withContext(Dispatchers.Main) {
                        Toast.makeText(activity, "Message sent to ${contact.name}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(activity, "Failed to send message to ${contact.name}", Toast.LENGTH_SHORT).show()
                    }
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
                Toast.makeText(activity, "Permission granted. You can now send SOS messages!", Toast.LENGTH_SHORT).show()
                sendEmergencySms()
            } else {
                Toast.makeText(activity, "SMS permission denied. Cannot send messages.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
