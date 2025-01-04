package com.example.csa_gdgc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.csa_gdgc.databinding.FragmentSosMainFragBinding
import com.example.csa_gdgc.sos_frags.SosSplashOutFragment

class SosMainFragment : Fragment() {

    private var _binding: FragmentSosMainFragBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSosMainFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.requestBtn.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag(SosSplashOutFragment::class.java.simpleName) == null) {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container_view, SosSplashOutFragment())
                    .commit()
            }
        }

        binding.backBtn.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, HomeFragment())
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
