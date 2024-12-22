package com.example.campussafetyapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController


class FragSafetyInfo : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_frag_safety_info, container, false)

       val skipButton = view.findViewById<ImageButton>(R.id.skipSafetyInfo)

        skipButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragSafetyInfo_to_fragAcedamicInfo)
        }

        return view
    }


}