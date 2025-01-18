package com.example.campussafetyapp.HomePageWork

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafetyapp.MyContactActivity
import com.example.campussafetyapp.R
import com.example.campussafetyapp.SosEmergencyActivity
import com.example.campussafetyapp.databinding.ActivityForgotPasswordBinding
import com.example.campussafetyapp.databinding.ActivityHomePageMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomePageMain : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page_main)





//

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

                    //Handle settings Button onClick
                    true
                }
                else->false
            }
        }



        val shareLiveLoc = findViewById<ImageButton>(R.id.shareLiveLoc)

        shareLiveLoc.setOnClickListener {
            //Share live location
        }



        val EmargencyContact = findViewById<CardView>(R.id.EmergencyContacts)
        EmargencyContact.setOnClickListener {
            //Emargency Contact
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
}