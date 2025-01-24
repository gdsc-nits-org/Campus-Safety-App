package com.example.campussafetyapp.UptoSOS

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.campussafetyapp.LoginWork.LoginActivity
import com.example.campussafetyapp.R


class SOSFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

       val view= inflater.inflate(R.layout.fragment_s_o_s, container, false)

        val startButton = view.findViewById<ImageButton>(R.id.getStartedButton)

        startButton.setOnClickListener{


            Intent(requireContext(), LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        return view
    }


}