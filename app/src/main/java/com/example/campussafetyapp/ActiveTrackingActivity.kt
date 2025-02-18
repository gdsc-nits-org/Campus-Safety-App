package com.example.campussafetyapp

import Contact
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.campussafetyapp.databinding.ActivityActiveTrackingBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration
import org.osmdroid.views.overlay.Marker
import org.osmdroid.util.GeoPoint
import android.preference.PreferenceManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

class ActiveTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActiveTrackingBinding
    private lateinit var selectedContactNumbers: MutableList<String>

//    private lateinit var contactsAdapter: ContactsAdapter  // ✅ Declare adapter at class level
    private lateinit var trackingAdapter: TrackingAdapter

    private var selectedContacts: MutableList<Contact> = mutableListOf() // ✅ Properly initialized

    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // Initialize OSMDroid
//        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
//
//        mapView = binding.map
//        mapView.setMultiTouchControls(true)
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        getUserLocation()
        // Initialize OSMDroid
        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))

        mapView = binding.map
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK) // ✅ Set tile source
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(18.0) // ✅ Ensure a default zoom level

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getUserLocation()

        selectedContactNumbers = mutableListOf()

        trackingAdapter = TrackingAdapter(
            selectedContacts,
            { contactToRemove -> removeContact(contactToRemove) }
        )

        val exitText = findViewById<TextView>(R.id.goBack)
        exitText.setOnClickListener {
            finish()
        }



        binding.rvSelectedContacts.adapter = trackingAdapter
        binding.rvSelectedContacts.layoutManager = LinearLayoutManager(this)


//        // Register Activity Result Launcher

        val contactPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val newContacts = result.data?.getParcelableArrayListExtra<Contact>("selectedContacts") ?: arrayListOf()

                // Preserve contact details, remove duplicates, and sort alphabetically
                val updatedContacts = (selectedContacts + newContacts)
                    .distinctBy { it.number } // Remove duplicates based on phone number
                    .sortedBy { it.name } // Sort alphabetically by name

                selectedContacts = updatedContacts.toMutableList()
                trackingAdapter.updateContacts(selectedContacts)
            }
        }




        binding.btnSelectContacts.setOnClickListener {
            contactPickerLauncher.launch(Intent(this, ContactSelectionActivity::class.java))
        }

        binding.btnShareLocation.setOnClickListener {
            shareLocationWithContacts()
        }
    }

    private fun removeContact(contact: Contact) {
        selectedContacts.removeAll { it.number == contact.number }
        trackingAdapter.updateContacts(selectedContacts)
    }


    private fun getUserLocation() {
        if (!hasLocationPermission()) {
            return
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geoPoint = GeoPoint(location.latitude, location.longitude)
                mapView.controller.setCenter(geoPoint)
                mapView.controller.setZoom(18.0)

                val marker = Marker(mapView)
                marker.position = geoPoint
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "You are here"
                mapView.overlays.add(marker)
                mapView.invalidate()
            } else {
                Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareLocationWithContacts() {
        if (!hasLocationPermission()) {
            Toast.makeText(this, "Location permission required to share location.", Toast.LENGTH_SHORT).show()
            return
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val locationLink = "https://maps.google.com/?q=${location.latitude},${location.longitude}"
                val message = "I am in danger. My live location is: $locationLink"

                if (selectedContacts.isEmpty()) {
                    Toast.makeText(this, "No contacts selected!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                selectedContacts.forEach { contact ->
                    sendSms(contact.number, message)
                }


                Toast.makeText(this, "Location shared successfully!", Toast.LENGTH_SHORT).show()
                finish()
//                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Unable to fetch location. Try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun sendSms(contactNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(contactNumber, null, message, null, null)
    }
    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}