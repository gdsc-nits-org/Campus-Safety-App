package com.example.campussafetyapp.HomePageWork

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafetyapp.EmergencyContacts.MyContactActivity
import com.example.campussafetyapp.R
import com.example.campussafetyapp.SoSEmergencyImplements.SosEmergencyActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.Manifest
import android.view.View
import android.widget.PopupMenu
import com.example.campussafetyapp.Developers.DevSplashScreen
import com.example.campussafetyapp.EmergencyContacts.EmergencyContactsActivity


class HomePageMain : AppCompatActivity() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page_main)


        //TopAppBar
        val topBar = findViewById<MaterialToolbar>(R.id.topBar)

        //Align System BARS
        ViewCompat.setOnApplyWindowInsetsListener(topBar) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, statusBarHeight, 0, 0)
            insets
        }
        topBar.setOnMenuItemClickListener{ menuItem->

            when(menuItem.itemId){
                R.id.notifications->{

                    //Handle notification Button onClick
                    true
                }
                R.id.settings->{

                    // Open SettingsActivity when settings button is clicked
                    val settingsView = findViewById<View>(R.id.settings)
                    showSettingsMenu(settingsView)

                    true
                }
                else->false
            }
        }

        //Open Street Map
        // Check and request GPS permission before opening the map
        val imageView = findViewById<ImageView>(R.id.imageView3)
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


        val shareLiveLoc = findViewById<ImageButton>(R.id.shareLiveLoc)

        shareLiveLoc.setOnClickListener {
            //Share live location
        }



        val EmargencyContact = findViewById<CardView>(R.id.EmergencyContacts)
        EmargencyContact.setOnClickListener {
            //Emargency Contact
            Intent(this, EmergencyContactsActivity::class.java).also {
                startActivity(it)
            }
        }




        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottomNavView.background=null


      //  val bottomAppBar : BottomAppBar = findViewById(R.id.bottomAppBar)


        bottomNavView.setOnItemSelectedListener { menuItem->

            when(menuItem.itemId){

                R.id.home->{
                    Toast.makeText(this, "Home Button Clicked", Toast.LENGTH_SHORT).show()
                    //Handle home Button onClick
                    true
                }
                R.id.Contacts->{
                    //Handle Contacts Button onClick
                  //  Toast.makeText(this, "Contacts Button Clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MyContactActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Campus->{
                    Toast.makeText(this, "CampusButton Clicked", Toast.LENGTH_SHORT).show()
                    //Handle campus Button onClick
                    true
                }
                R.id.Academics->{
                    //Handle Contacts Button onClick
                    Toast.makeText(this, "Academics Button Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else->false
            }

        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            //Handle FAB onClick
            val intent = Intent(this, SosEmergencyActivity::class.java)
            startActivity(intent)
            //Toast.makeText(this, "Fab Button Clicked", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showSettingsMenu(anchor: View) {

        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.menu_main, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.developers -> {
                    var intent = Intent(this,DevSplashScreen::class.java)
                    startActivity(intent)
                    true
                }
                R.id.feedback -> {
                    Toast.makeText(this, "Privacy Policy Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.whats_new -> {
                    Toast.makeText(this, "What's New Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }


    // Open the OpenStreetMap activity
    private fun openOpenStreetMapActivity() {
        val intent = Intent(this, OpenStreetMapActivity::class.java)
        startActivity(intent)
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openOpenStreetMapActivity()
            } else {
                Toast.makeText(this, "GPS permission is required to proceed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}