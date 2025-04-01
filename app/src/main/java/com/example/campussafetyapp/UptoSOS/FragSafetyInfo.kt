package com.example.campussafetyapp.UptoSOS

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.example.campussafetyapp.R


class FragSafetyInfo : Fragment() {

    private lateinit var progressBar: ProgressBar
    private val duration = 5000L // 5 seconds duration
    private var currentAnimator: ObjectAnimator? = null
    private var isPaused = false


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_frag_safety_info, container, false)

       val skipButton = view.findViewById<ImageButton>(R.id.skipSafetyInfo)
        val nextArrow = view.findViewById<ImageButton>(R.id.arrow_next_safety)

        nextArrow.setOnClickListener {

            if(isAdded) {
            findNavController().navigate(R.id.action_fragSafetyInfo_to_fragAcedamicInfo)
             }
        }

        skipButton.setOnClickListener {
//             Intent(requireContext(), LoginActivity_primary::class.java).also {
//                 startActivity(it)
//
//             }
               if(isAdded) {
                   findNavController().navigate(R.id.action_fragSafetyInfo_to_loginActivity)
               }
        }


        progressBar = view.findViewById(R.id.singleProgressBar)
        val constraintLayout : ConstraintLayout = view.findViewById(R.id.contraa)
        // Set touch listener for pausing/resuming
        constraintLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> pauseProgress()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> resumeProgress()
            }
            true
        }

        startProgressBar()


        return view
    }


    private fun startProgressBar() {
        currentAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100).apply {
            duration = this@FragSafetyInfo.duration
            interpolator = LinearInterpolator()
            start()

//            doOnEnd {
//               // Toast.makeText(this@FragSafetyInfo.requireContext()    , "Progress Completed!", Toast.LENGTH_SHORT).show()
//                findNavController().navigate(R.id.action_fragSafetyInfo_to_fragAcedamicInfo)
//            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {

                    if(isAdded) {
                        findNavController().navigate(R.id.action_fragSafetyInfo_to_fragAcedamicInfo)
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                   if(isAdded){
                       findNavController().navigate(R.id.action_fragSafetyInfo_to_fragAcedamicInfo)
                    }

                }
            })
        }
    }

    private fun pauseProgress() {
        if (isPaused || currentAnimator==null) return
        isPaused = true
        currentAnimator?.pause()
    }

    private fun resumeProgress() {
        if (!isPaused|| currentAnimator==null) return
        isPaused = false
        currentAnimator?.resume()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        currentAnimator?.cancel()
    }




}