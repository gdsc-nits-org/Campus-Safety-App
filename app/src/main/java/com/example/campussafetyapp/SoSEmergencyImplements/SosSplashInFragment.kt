package com.example.campussafetyapp.SoSEmergencyImplements

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.campussafetyapp.R
import com.example.campussafetyapp.databinding.FragmentSosSplashInBinding

class SosSplashInFragment : Fragment() {

    private var _binding : FragmentSosSplashInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSosSplashInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(requireContext(),R.anim.sos_splash_in)
        binding.circle.startAnimation(animation)
        Handler(Looper.getMainLooper()).postDelayed({
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.sos_fragment_container, SosMainFragment())
                .commit()
        }, 2000)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}