package com.example.csa_gdgc.sos_frags

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.csa_gdgc.R
import com.example.csa_gdgc.SosMainFragment
import com.example.csa_gdgc.databinding.FragmenrSosInSplashBinding

class SosSplashInFragment : Fragment() {

    private var _binding : FragmenrSosInSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmenrSosInSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(requireContext(),R.anim.sos_splash_in)
        binding.circle.startAnimation(animation)
        Handler(Looper.getMainLooper()).postDelayed({
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, SosMainFragment())
                .commit()
        }, 2000)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}