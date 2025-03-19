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
            Developer(
                name = "Draksha Chaudhary",
                branch = "Branch: CSE",
                imageResId = R.drawable.draksha,
                facebookLink = "https://www.facebook.com/draksha.chaudhary/",
                instagramLink = "https://www.instagram.com/cdraksha",
                linkedInLink = "https://www.linkedin.com/in/draksha-chaudhary",
                gitHubLink = "https://github.com/drakshaa"
            ),
            Developer(
                name = "Draksha ",
                branch = "Branch: ECE",
                imageResId = R.drawable.sample_image,
                facebookLink = "https://www.facebook.com/draksha.chaudhary/",
                instagramLink = "https://www.instagram.com/cdraksha",
                linkedInLink = "https://www.linkedin.com/in/draksha-chaudhary",
                gitHubLink = "https://github.com/drakshaa"
            )
        )

        val adapter = DeveloperAdapter(this, developers)
        recyclerView.adapter = adapter
    }
}
