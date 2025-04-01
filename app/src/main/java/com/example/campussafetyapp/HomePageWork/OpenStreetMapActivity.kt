package com.example.campussafetyapp.HomePageWork

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.campussafetyapp.R
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.File

class OpenStreetMapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var btnHospital: Button
    private lateinit var btnPolice: Button
    private lateinit var btnZoomIn: ImageButton
    private lateinit var btnZoomOut: ImageButton
    private lateinit var progressBar: ProgressBar
    private var userLocation: GeoPoint? = null
    private val hospitalMarkers = mutableListOf<Marker>()
    private val policeMarkers = mutableListOf<Marker>()
    private var isHospitalVisible = false
    private var isPoliceVisible = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))
        setContentView(R.layout.activity_open_street_map)


        val cacheDir = File(filesDir, "osmdroid")
        Configuration.getInstance().osmdroidBasePath = cacheDir
        Configuration.getInstance().osmdroidTileCache = File(cacheDir, "tiles")


        mapView = findViewById(R.id.map)
        btnHospital = findViewById(R.id.btn_hospital)
        btnPolice = findViewById(R.id.btn_police)
        btnZoomIn = findViewById(R.id.btn_zoom_in)
        btnZoomOut = findViewById(R.id.btn_zoom_out)
        progressBar = findViewById(R.id.loader)

        // Initialize map
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)
        val mapController = mapView.controller
        mapController.setZoom(15.0)

        // Request location updates
        setupLocationListener()

        // Handle button clicks
        btnHospital.setOnClickListener {
            toggleMarkers("hospital", hospitalMarkers, isHospitalVisible) { isHospitalVisible = it }
        }

        btnPolice.setOnClickListener {
            toggleMarkers("police", policeMarkers, isPoliceVisible) { isPoliceVisible = it }
        }

        btnZoomIn.setOnClickListener {
            if (canZoomIn()) {
                mapView.controller.zoomIn()
            } else {
                Toast.makeText(this, "Cannot zoom in further", Toast.LENGTH_SHORT).show()
            }
        }

        btnZoomOut.setOnClickListener {
            if (canZoomOut()) {
                mapView.controller.zoomOut()
            } else {
                Toast.makeText(this, "Cannot zoom out further", Toast.LENGTH_SHORT).show()
            }
        }

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomePageMain::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupLocationListener() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                userLocation = GeoPoint(location.latitude, location.longitude)
                val overlay = MyLocationNewOverlay(mapView)
                overlay.enableMyLocation()

                mapView.overlays.add(overlay)
                mapView.controller.setCenter(userLocation)
                progressBar.visibility = View.GONE
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                10f,
                locationListener
            )
        }
    }

    private fun toggleMarkers(
        type: String,
        markerList: MutableList<Marker>,
        isVisible: Boolean,
        updateVisibilityFlag: (Boolean) -> Unit
    ) {
        if (isVisible) {
            // Hide markers
            mapView.overlays.removeAll(markerList)
            markerList.clear()
        } else {
            // Show markers
            userLocation?.let {
                progressBar.visibility = View.VISIBLE
                fetchNearbyPlaces(type, it.latitude, it.longitude) { newMarkers ->
                    markerList.addAll(newMarkers)
                    progressBar.visibility = View.GONE
                }
            } ?: Toast.makeText(this, "Waiting for location...", Toast.LENGTH_SHORT).show()
        }
        mapView.invalidate()
        updateVisibilityFlag(!isVisible)
    }

    private fun fetchNearbyPlaces(type: String, latitude: Double, longitude: Double, callback: (List<Marker>) -> Unit) {
        val boundingBoxSize = 0.05 // ~5km radius
        val query = """
            [out:json];
            node["amenity"="$type"](${latitude - boundingBoxSize},${longitude - boundingBoxSize},${latitude + boundingBoxSize},${longitude + boundingBoxSize});
            out;
        """.trimIndent()

        val url = "https://overpass-api.de/api/interpreter?data=$query"

        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                callback(displayMarkers(response, type))
            },
            {
                Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        )
        queue.add(stringRequest)
    }

    private fun displayMarkers(response: String, type: String): List<Marker> {
        val elements = JSONObject(response).getJSONArray("elements")
        val markers = mutableListOf<Marker>()

        for (i in 0 until elements.length()) {
            val element = elements.getJSONObject(i)
            val lat = element.getDouble("lat")
            val lon = element.getDouble("lon")
            val name = element.optJSONObject("tags")?.optString("name", "Unknown") ?: "Unknown"
            markers.add(addMarker(lat, lon, name, type))
        }

        return markers
    }

    private fun addMarker(latitude: Double, longitude: Double, name: String, type: String): Marker {
        val marker = Marker(mapView)
        marker.position = GeoPoint(latitude, longitude)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        // Use vector asset for hospital and police
        marker.icon = when (type) {
            "hospital" -> resources.getDrawable(R.drawable.local_hospital, null)
            "police" -> resources.getDrawable(R.drawable.local_police, null)
            else -> resources.getDrawable(android.R.drawable.ic_menu_mapmode, null)
        }

        // Calculate distance and set title with distance in kilometers
        val distance = userLocation?.distanceToAsDouble(marker.position)?.div(1000.0)
        val distanceText = distance?.let { "%.2f km".format(it) } ?: "Unknown distance"
        marker.title = "$name ($distanceText)"

        mapView.overlays.add(marker)
        mapView.invalidate()
        return marker
    }

    private fun GeoPoint.distanceToAsDouble(other: GeoPoint): Double {
        val results = FloatArray(1)
        Location.distanceBetween(this.latitude, this.longitude, other.latitude, other.longitude, results)
        return results[0].toDouble()
    }

    // Function to check if zooming in is possible
    private fun canZoomIn(): Boolean {
        return mapView.zoomLevel < mapView.maxZoomLevel
    }

    // Function to check if zooming out is possible
    private fun canZoomOut(): Boolean {
        return mapView.zoomLevel > mapView.minZoomLevel
    }
}