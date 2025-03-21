package com.example.campussafetyapp.Developers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campussafetyapp.R

class DevelopersPage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developers_page)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val developers = listOf(
            DeveloperItem.SectionHeader("Android Developers"),

            DeveloperItem.Developer(
                name = "Abhishekh Kumar Mishra",
                details = "Android Developer",
                branch = "NIT Silchar | CSE",
                imageResId = R.drawable.abhisekh_mishra,
                facebookLink = "https://www.facebook.com/profile.php?id=100055101542369",
                instagramLink = "https://www.instagram.com/iamabhishekhmishra/?hl=en",
                linkedInLink = "https://www.linkedin.com/in/abhishekh-kumar-mishra-81a655291/",
                gitHubLink = "https://github.com/Abhishekhmishra0"
            ),
            DeveloperItem.Developer(
                name = "Draksha Chaudhary",
                details = "Android Developer",
                branch = "NIT Silchar | CSE",
                imageResId = R.drawable.draksha,
                facebookLink = "https://www.facebook.com/draksha.chaudhary/",
                instagramLink = "https://www.instagram.com/cdraksha",
                linkedInLink = "https://www.linkedin.com/in/draksha-chaudhary",
                gitHubLink = "https://github.com/drakshaa"
            ),
            DeveloperItem.Developer(
                name = "Parishmita Banik",
                details = "Android Developer",
                branch = "NIT Silchar | CSE",
                imageResId = R.drawable.parishmita,
                facebookLink = "https://www.facebook.com/share/1ZJyH49KmG/",
                instagramLink = "https://www.instagram.com/banikparishmita/",
                linkedInLink = "https://www.linkedin.com/in/parishmita-banik-8b0235288/",
                gitHubLink = "https://github.com/Parishmitabanik"
            ),
            DeveloperItem.Developer(
                name = "Shaik Mahammad Razeef ",
                branch = "NIT Silchar | EE",
                imageResId = R.drawable.razeef,
                details = "Android Developer",
                facebookLink = "https://www.facebook.com/",
                instagramLink = "https://www.instagram.com/razeef_shaik?igsh=cGE5ZXY4cnk2MWl5",
                linkedInLink = "https://www.linkedin.com/in/shaik-mahammad-razeef-374a4b2a4?",
                gitHubLink = "https://github.com/Razeefshaik"

            ),
            DeveloperItem.SectionHeader("UI/UX Developers"),
            DeveloperItem.Developer(
                name = "... ",
                branch = "NIT Silchar | EE",
                imageResId = R.drawable.sample_image,
                details = "Android Developer",
                facebookLink = "https://www.facebook.com/",
                instagramLink = "https://www.instagram.com/",
                linkedInLink = "https://www.linkedin.com/in/",
                gitHubLink = "https://github.com/"

            )
        )

        val adapter = DeveloperAdapter(this, developers)
        recyclerView.adapter = adapter
    }
}
