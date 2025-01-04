package com.example.campussafetyapp.sos_fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.campussafetyapp.R
import com.example.campussafetyapp.databinding.FragmentSosSplashOutBinding

class SosSplashOutFragment : Fragment() {

    private var _binding : FragmentSosSplashOutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSosSplashOutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(requireContext(),R.anim.sos_splash_out)
        binding.ambulance.startAnimation(animation)
//        Handler(Looper.getMainLooper()).postDelayed({
//           // To be defined later
//        }, 2000)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}