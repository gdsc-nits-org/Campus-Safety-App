package com.example.campussafetyapp.TaskManager

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.campussafetyapp.R
import com.example.campussafetyapp.databinding.ItemCalenderDayBinding
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.sample.shared.displayText
import com.kizitonwose.calendar.sample.shared.getWeekPageTitle
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.YearMonth

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY id DESC")
    fun getTasksByDate(date: String): LiveData<List<Task>>

    @Delete
    suspend fun deleteTask(task :Task)
}




//viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
//binding = ActivityTaskManagerBinding.inflate(layoutInflater)
////        calendarView = findViewById(R.id.calendarView)
////
////        val addTaskFab = findViewById<FloatingActionButton>(R.id.addTaskFab)
////        addTaskFab.setOnClickListener {
////            showAddTaskDialog()
////        }
//
//// setupCalendar()
//
//
//class DayViewContainer(view: View) : ViewContainer(view) {
//    val bind = ItemCalenderDayBinding.bind(view)
//    lateinit var day: WeekDay
//
//    init {
//        view.setOnClickListener {
//            if (selectedDate != day.date) {
//                val oldDate = selectedDate
//                selectedDate = day.date
//                binding.exSevenCalendar.notifyDateChanged(day.date)
//                oldDate?.let { binding.exSevenCalendar.notifyDateChanged(it) }
//            }
//        }
//    }
//
//    fun bind(day: WeekDay) {
//        this.day = day
//        bind.exSevenDateText.text = dateFormatter.format(day.date)
//        bind.exSevenDayText.text = day.date.dayOfWeek.displayText()
//
//        val colorRes = if (day.date == selectedDate) {
//            R.color.arrow2
//        } else {
//            R.color.white
//        }
//        bind.exSevenDateText.setTextColor(view.context.getColor(colorRes))
//        bind.exSevenSelectedView.isVisible = day.date == selectedDate
//    }
//}
//
//binding.exSevenCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
//    override fun create(view: View) = DayViewContainer(view)
//    override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
//}
//
//binding.exSevenCalendar.weekScrollListener = { weekDays ->
//    binding.exSevenToolbar.title = getWeekPageTitle(weekDays)
//}
//
//val currentMonth = YearMonth.now()
//binding.exSevenCalendar.setup(
//currentMonth.minusMonths(5).atStartOfMonth(),
//currentMonth.plusMonths(5).atEndOfMonth(),
//firstDayOfWeekFromLocale(),
//)
//binding.exSevenCalendar.scrollToDate(LocalDate.now())
//}
//}


//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun setupCalendar() {
//
//        val today = LocalDate.now()
//        val startMonth = YearMonth.from(today.minusMonths(1)) // Convert LocalDate to YearMonth
//        val endMonth = YearMonth.from(today.plusMonths(1))
//
//        calendarView.setup(startMonth, endMonth, DayOfWeek.MONDAY)
//        calendarView.scrollToMonth(YearMonth.from(today)) // Ensure it scrolls to the current month
//        calendarView.scrollToDate(today)
//
//        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
//            override fun create(view: View) = DayViewContainer(view)
//
//            override fun bind(container: DayViewContainer, data: CalendarDay) {
//                container.textView.text = data.date.dayOfMonth.toString()
//
//                // Highlight selected date
//                container.textView.setOnClickListener {
//                    val selectedDate = data.date.toString()  // "YYYY-MM-DD"
//                    loadTasksForDate(selectedDate)
//                }
//            }
//        }
//    }

//    private fun loadTasksForDate(date: String) {
//        viewModel.getTasks(date).observe(this) { tasks ->
//            taskAdapter.submitList(tasks)
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun showAddTaskDialog() {
//        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
//
//        val dialog = AlertDialog.Builder(this)
//            .setView(dialogView)
//            .setTitle("Add Task")
//            .create()
//
//        val taskTitleInput = dialogView.findViewById<EditText>(R.id.taskTitleInput)
//        val addTaskButton = dialogView.findViewById<Button>(R.id.addTaskButton)
//
//        addTaskButton.setOnClickListener {
//            val taskTitle = taskTitleInput.text.toString()
//            if (taskTitle.isNotEmpty()) {
//                val selectedDate = LocalDate.now().toString()
//                val task = Task(title = taskTitle, description = "", date = selectedDate)
//                viewModel.addTASK(task)
//                dialog.dismiss()
//            } else {
//                taskTitleInput.error = "Task title cannot be empty"
//            }
//        }
//
//        dialog.show()
//    }