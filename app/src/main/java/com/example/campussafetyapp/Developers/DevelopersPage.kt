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
            DeveloperItem.Developer(
                name = "Gauri Jain",
                details = "Android Developer",
                branch = "NIT Silchar | CSE",
                imageResId = R.drawable.gauri_jain,
                facebookLink = "https://www.facebook.com/share/1AHMCTDmLj/",
                instagramLink = "https://www.instagram.com/gaurijainn_?igsh=MWdkenI4cW16bXlhaA==",
                linkedInLink = "https://www.linkedin.com/in/gauri-jain-545740312?",
                gitHubLink = "https://github.com/gaurijainn"
            ),
            DeveloperItem.Developer(
                name = "Bishal Sarma",
                details = "Android Developer",
                branch = "NIT Silchar | ECE",
                imageResId = R.drawable.bishal_sarma,
                facebookLink = "https://www.facebook.com/vishal.jimon",
                instagramLink = "https://www.instagram.com/___b1shal_svs/",
                linkedInLink = "https://www.linkedin.com/in/bishal-sarma-41a9b128a/",
                gitHubLink = "https://github.com/svsBishal"
            ),
            DeveloperItem.Developer(
                name = "Shibam Singh",
                details = "Android Developer",
                branch = "NIT Silchar | CSE",
                imageResId = R.drawable.shibam_singh,
                facebookLink = "https://www.facebook.com/shibam.yadav.3154",
                instagramLink = "https://www.instagram.com/shibam_singh.23/",
                linkedInLink = "https://www.linkedin.com/in/shibam-singh/",
                gitHubLink = "https://github.com/shibamsingh/"
            ),
            DeveloperItem.Developer(
                name = "Kunaljit Kasyap",
                details = "Android Developer",
                branch = "NIT Silchar | EE",
                imageResId = R.drawable.kunaljit_kasyap,
                facebookLink = "https://www.facebook.com/proshifterkunal.proshifterkunal",
                instagramLink = "https://www.instagram.com/heykunalll__/",
                linkedInLink = "https://www.linkedin.com/in/kunaljit-kashyap-8bb212287/",
                gitHubLink = "https://github.com/kunaljit3006"
            ),
            DeveloperItem.SectionHeader("UI/UX Developers"),
            DeveloperItem.Developer(
                name = "Piyush Chatterjee",
                branch = "NIT Silchar | CSE",
                imageResId = R.drawable.piyush_chatterjee,
                details = "UI/UX Developer",
                facebookLink = "https://www.facebook.com/",
                instagramLink = "https://www.instagram.com/",
                linkedInLink = "https://www.linkedin.com/in/piyush-chatterjee-1b501928b?",
                gitHubLink = "https://github.com/Piyush-Chatterjee"

            ),
            DeveloperItem.Developer(
                name = "Manisha Saloni",
                branch = "NIT Silchar | ECE",
                imageResId = R.drawable.manisha_saloi,
                details = "UI/UX Developer",
                facebookLink = "https://www.facebook.com/profile.php?id=61550675969308&mibextid=ZbWKwL",
                instagramLink = "https://www.instagram.com/manisha_saloi?igsh=MnNrbjg2bjU0bHBp",
                linkedInLink = "https://www.linkedin.com/in/manisha-saloi-aa564228a/",
                gitHubLink = "https://github.com/manisha999404"

            ),
            DeveloperItem.Developer(
                name = "Jitamanyu Phukan",
                branch = "NIT Silchar | ECE",
                imageResId = R.drawable.jitmanyu_phukan,
                details = "UI/UX Developer",
                facebookLink = "https://www.facebook.com/profile.php?id=100070630387563",
                instagramLink = "https://www.instagram.com/sillycon321?igsh=cHc2ZnozdGlpMTdu",
                linkedInLink = "www.linkedin.com/in/jitamanyu-phukan-562728280",
                gitHubLink = "https://github.com/procoast123"

            )
        )

        val adapter = DeveloperAdapter(this, developers)
        recyclerView.adapter = adapter
    }
}
