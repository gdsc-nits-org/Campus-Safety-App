package com.example.campussafetyapp.SMSEmergencyContacts

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.campussafetyapp.RoomDB.AppDatabase
import com.example.campussafetyapp.RoomDB.Contact
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class SendSosSms(private val activity: Activity) {

    private val REQUEST_CODE_SMS = 1
    private val REQUEST_CODE_LOCATION = 2
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    // Sends an SOS message to all emergency contacts in the database.
    fun sendEmergencySms() {
        // Check SMS permission
        if (!hasSmsPermission()) {
            requestSmsPermission()
            return
        }

        // Check location permission
        if (!hasLocationPermission()) {
            requestLocationPermission()
            return
        }

        // Fetch user location and send messages
        CoroutineScope(Dispatchers.IO).launch {
            val location = getUserLocation()
            val locationLink = location?.let {
                "https://maps.google.com/?q=${it.latitude},${it.longitude}"
            } ?: "Location unavailable"

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
            val failedContacts = mutableListOf<String>()

            for (contact in contacts) {
                try {
                    // Validate phone number before sending
                    if (!isValidPhoneNumber(contact.phoneNumber)) {
                        failedContacts.add(contact.name ?: "Unknown")
                        continue
                    }

                    val message = "This is an emergency message. My location: $locationLink"
                    smsManager.sendTextMessage(
                        contact.phoneNumber,
                        null,
                        message,
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

    // Checks if the app has location permissions.
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Requests location permissions from the user.
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE_LOCATION
        )
    }

    // Fetches the user's current location.
    private suspend fun getUserLocation(): Location? {
        return try {
            suspendCancellableCoroutine { continuation ->
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        continuation.resume(location)
                    }
                    .addOnFailureListener {
                        continuation.resume(null)
                    }
            }
        } catch (e: SecurityException) {
            null // Return null if permission is not granted
        }
    }

    // Validates phone numbers to ensure they are correct.
    private fun isValidPhoneNumber(phoneNumber: String?): Boolean {
        return phoneNumber != null && android.util.Patterns.PHONE.matcher(phoneNumber).matches()
    }

    // Handles the result of the permissions request.
    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_SMS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "SMS permission granted. Sending SOS messages!", Toast.LENGTH_SHORT).show()
                sendEmergencySms()
            } else {
                Toast.makeText(activity, "SMS permission denied. Cannot send messages.", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendEmergencySms()
            } else {
                Toast.makeText(activity, "Location permission denied. Cannot fetch location.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
