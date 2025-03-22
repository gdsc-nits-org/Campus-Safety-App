package com.example.campussafetyapp.TaskManager

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.campussafetyapp.R
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate

class DayViewContainer(view: View) : ViewContainer(view) {
   // val textView: TextView = view.findViewById(R.id.calendarDayText) // Reference to TextView in item_calendar_day.xml

    lateinit var date: LocalDate  // Store the date for later use

    init {
        view.setOnClickListener {
            // Handle date click event
            Toast.makeText(view.context, "Selected: $date", Toast.LENGTH_SHORT).show()
        }
    }
}