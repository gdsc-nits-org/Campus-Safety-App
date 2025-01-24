package com.example.campussafetyapp.SoSEmergencyImplements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.campussafetyapp.R
import com.example.campussafetyapp.SMSEmergencyContacts.SendSosSms
import com.example.campussafetyapp.databinding.FragmentSosMainBinding

class SosMainFragment : Fragment() {

    private var _binding: FragmentSosMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var sendSosSmsHelper: SendSosSms


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSosMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val sendSosSmsHelper = SendSosSms(requireActivity())

            binding.requestBtn.setOnClickListener {
                // Send SOS SMS
                sendSosSmsHelper.sendEmergencySms()
                if (parentFragmentManager.findFragmentByTag(SosSplashOutFragment::class.java.simpleName) == null) {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.sos_fragment_container, SosSplashOutFragment(), SosSplashOutFragment::class.java.simpleName)
                        .commit()
                }
            }


        binding.backBtn.setOnClickListener {
            parentFragmentManager
                // To be defined later
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::sendSosSmsHelper.isInitialized) {
            sendSosSmsHelper.handlePermissionsResult(requestCode, grantResults)
        }
    }
}
