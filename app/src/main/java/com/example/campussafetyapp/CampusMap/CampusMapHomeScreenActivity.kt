package com.example.campussafetyapp.CampusMap

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafetyapp.HomePageWork.HomePageMain.Companion.REQUEST_LOCATION_PERMISSION
import com.example.campussafetyapp.HomePageWork.OpenStreetMapActivity
import com.example.campussafetyapp.R

class CampusMapHomeScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campus_map_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Open Street Map
        // Check and request GPS permission before opening the map
        val imageView = findViewById<ImageView>(R.id.imageView31)
        imageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted, open the map activity
                openOpenStreetMapActivity()
            } else {
                // Request GPS permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }
        }


        val btnAdmin = findViewById<CardView>(R.id.btnAdmin)
        val btnCanteen = findViewById<CardView>(R.id.btnCanteen)
        val btnSbiBank = findViewById<CardView>(R.id.btnSbiBank)
        val btnPark = findViewById<CardView>(R.id.btnPark)
        val btnSAC = findViewById<CardView>(R.id.btnSAC)
        val btnHealthCenter = findViewById<CardView>(R.id.btnHealthCenter)
        val btnSportsComplex = findViewById<CardView>(R.id.btnSportsComplex)

        // Define destination locations (latitude, longitude)
        val adminLocation = "24.758849399743426, 92.79428756557057"
        val canteenLocation = "24.75794249480779, 92.78987628221752"
        val sbiBankLocation = "24.757863054445913, 92.79031457621305"
        val parkLocation = "24.754119532125518, 92.7903070998921"
        val sacLocation = "24.75703500511771, 92.78868195590317"
        val healthCenterLocation = "24.755312550367428, 92.78946018091372"
        val sportsComplexLocation = "24.756090908883166, 92.78331331984018"

        // Set onClickListeners for each button to open the map activity
        btnAdmin.setOnClickListener { openMap(adminLocation) }
        btnCanteen.setOnClickListener { openMap(canteenLocation) }
        btnSbiBank.setOnClickListener { openMap(sbiBankLocation) }
        btnPark.setOnClickListener { openMap(parkLocation) }
        btnSAC.setOnClickListener { openMap(sacLocation) }
        btnHealthCenter.setOnClickListener { openMap(healthCenterLocation) }
        btnSportsComplex.setOnClickListener { openMap(sportsComplexLocation) }
    }

    private fun openMap(destination: String) {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("DESTINATION", destination)
        startActivity(intent)
    }
    // Open the OpenStreetMap activity
    private fun openOpenStreetMapActivity() {
        val intent = Intent(this, OpenStreetMapActivity::class.java)
        startActivity(intent)
    }
}
