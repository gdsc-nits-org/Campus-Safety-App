package com.example.campussafetyapp.CampusMap

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.net.URL
import kotlin.concurrent.thread
import com.example.campussafetyapp.R

class MapActivity : AppCompatActivity() {
    private lateinit var map: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var userLocation: GeoPoint? = null
    private var destinationLocation: GeoPoint? = null
    private var routePolyline: Polyline? = null
    private var isFirstLocationUpdate = true // Track if this is the first location update

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osm", MODE_PRIVATE))
        setContentView(R.layout.activity_map)


        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        // Get destination coordinates and name from intent
        val destinationData = intent.getStringExtra("DESTINATION")!!.split(",")
        destinationLocation = GeoPoint(destinationData[0].toDouble(), destinationData[1].toDouble())
        val destinationName = intent.getStringExtra("DESTINATION_NAME") ?: "Destination"

        // Location service setup
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates(destinationName)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        // Refresh route button
        findViewById<Button>(R.id.refresh_button).setOnClickListener {
            fetchRoute() // Manually fetch route when button is clicked
        }
    }

    private fun startLocationUpdates(destinationName: String) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    userLocation = GeoPoint(location.latitude, location.longitude)
                    updateMap(destinationName)

                    // Fetch route only on first location update
                    if (isFirstLocationUpdate) {
                        isFirstLocationUpdate = false
                        fetchRoute()
                    }
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    private fun updateMap(destinationName: String) {
        runOnUiThread {
            // Add user marker (update position if it exists)
            userLocation?.let {
                val userMarker = Marker(map)
                userMarker.position = it
                userMarker.title = "You"
                userMarker.icon = ContextCompat.getDrawable(this, R.drawable.you_marker)

                // Remove old marker if needed
                map.overlays.removeAll(map.overlays.filterIsInstance<Marker>().filter { it.title == "You" })
                map.overlays.add(userMarker)
            }

            // Add destination marker only once
            if (!map.overlays.any { it is Marker && it.title == destinationName }) {
                destinationLocation?.let {
                    val destinationMarker = Marker(map)
                    destinationMarker.position = it
                    destinationMarker.title = destinationName
                    destinationMarker.icon = ContextCompat.getDrawable(this, R.drawable.destination_marker)
                    map.overlays.add(destinationMarker)
                }
            }

            // Adjust zoom dynamically based on distance
            if (userLocation != null && destinationLocation != null) {
                val mapController = map.controller
                mapController.setZoom(18.0)  // Close zoom level
                mapController.setCenter(userLocation)  // Keep user centered
            }

            map.invalidate() // Refresh the map view
        }
    }

    private fun fetchRoute() {
        if (userLocation == null || destinationLocation == null) return // Prevent crash

        thread {
            val url = "https://router.project-osrm.org/route/v1/driving/${userLocation!!.longitude},${userLocation!!.latitude};${destinationLocation!!.longitude},${destinationLocation!!.latitude}?overview=full&geometries=geojson"
            try {
                val response = URL(url).readText()
                val json = JSONObject(response)
                val routesArray = json.getJSONArray("routes")

                if (routesArray.length() == 0) return@thread // No route available

                val coordinates = routesArray.getJSONObject(0)
                    .getJSONObject("geometry").getJSONArray("coordinates")

                val newPolyline = Polyline().apply {
                    width = 10f
                    color = Color.BLUE  // Route color
                }

                for (i in 0 until coordinates.length()) {
                    val point = coordinates.getJSONArray(i)
                    newPolyline.addPoint(GeoPoint(point.getDouble(1), point.getDouble(0)))
                }

                runOnUiThread {
                    // Remove old route if it exists
                    routePolyline?.let { map.overlays.remove(it) }
                    routePolyline = newPolyline
                    map.overlays.add(newPolyline)
                    map.invalidate() // Refresh map
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback) // Stop location updates when activity is destroyed
    }
}
