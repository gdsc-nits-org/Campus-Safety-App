package com.example.campussafetyapp.TaskManager

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campussafetyapp.R
import com.example.campussafetyapp.databinding.FragmentTaskManagerBinding
import com.example.campussafetyapp.databinding.ItemCalenderDayBinding
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.sample.shared.displayText
import com.kizitonwose.calendar.sample.shared.getWeekPageTitle
import com.kizitonwose.calendar.sample.view.BaseFragment
import com.kizitonwose.calendar.sample.view.HasBackButton
import com.kizitonwose.calendar.sample.view.HasToolbar
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class TaskManagerFrag : BaseFragment(R.layout.fragment_task_manager), HasToolbar, HasBackButton {
    override val titleRes: Int = R.string.example_7_title

    override val toolbar: Toolbar
        get() = binding.exSevenToolbar

    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

    private lateinit var binding: FragmentTaskManagerBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var viewModel: TaskViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaskManagerBinding.bind(view)
        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        setupRecyclerView()
        setupObservers()
        setupCalendar()

        binding.fabShowDialog.setOnClickListener {
            showAddTaskDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter { task ->
            AlertDialog.Builder(requireContext())
                .setMessage("Confirm Delete")
                .setPositiveButton("Delete") { _, _ ->
                    deleteEvent(task)
                }
                .setNegativeButton("Close", null)
                .show()
        }
        binding.exThreeRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = taskAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupObservers() {
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            updateTaskList(date)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCalendar() {
        class DayViewContainer(view: View) : ViewContainer(view) {
            val bind = ItemCalenderDayBinding.bind(view)
            lateinit var day: WeekDay

            init {
                view.setOnClickListener {
                    val newDate = day.date.toString()
                    val oldDate = viewModel.selectedDate.value

                    if (oldDate != newDate) {
                        viewModel.updateSelectedDate(newDate)
                        refreshCalendar(day.date, oldDate)
                    }
                }
            }

            fun bind(day: WeekDay) {
                this.day = day
                bind.exSevenDateText.text = dateFormatter.format(day.date)
                bind.exSevenDateText.setTextColor(view.context.getColor(R.color.black))
                bind.exSevenDayText.text = day.date.dayOfWeek.displayText()
                bind.exSevenDayText.setTextColor(view.context.getColor(R.color.black))

                val isSelected = day.date.toString() == viewModel.selectedDate.value
                val colorRes = if (isSelected) R.color.TASKcOL2 else R.color.white
                bind.TaskDayCol.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(view.context, colorRes)
                )
            }
        }

        binding.exSevenCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
        }

        binding.exSevenCalendar.weekScrollListener = { weekDays ->
            binding.exSevenToolbar.title = getWeekPageTitle(weekDays)
            view?.context?.let { binding.exSevenToolbar.setTitleTextColor(it.getColor(R.color.black)) }
        }

        val currentMonth = YearMonth.now()
        binding.exSevenCalendar.setup(
            currentMonth.minusMonths(5).atStartOfMonth(),
            currentMonth.plusMonths(5).atEndOfMonth(),
            firstDayOfWeekFromLocale(),
        )
        binding.exSevenCalendar.scrollToDate(LocalDate.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteEvent(task: Task) {
        viewModel.deleteTask(task).invokeOnCompletion {
            updateTaskList(viewModel.selectedDate.value ?: LocalDate.now().toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Add Task")
            .create()

        val taskTitleInput = dialogView.findViewById<EditText>(R.id.taskTitleInput)
        val addTaskButton = dialogView.findViewById<Button>(R.id.addTaskButton)

        addTaskButton.setOnClickListener {
            val taskTitle = taskTitleInput.text.toString()
            if (taskTitle.isNotEmpty()) {
                val selectedDate = viewModel.selectedDate.value ?: LocalDate.now().toString()
                val task = Task(title = taskTitle, description = "", date = selectedDate)

                viewModel.addTASK(task).invokeOnCompletion {
                    requireActivity().runOnUiThread {
                        updateTaskList(selectedDate)
                        refreshCalendar(LocalDate.parse(selectedDate), null)
                    }
                }
                dialog.dismiss()
            } else {
                taskTitleInput.error = "Task title cannot be empty"
            }
        }
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTaskList(date: String) {
        viewModel.getTasksByDate(date).observe(viewLifecycleOwner) { tasks ->
            taskAdapter.submitList(tasks)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshCalendar(newDate: LocalDate, oldDate: String?) {
        binding.exSevenCalendar.notifyDateChanged(newDate)
        oldDate?.let { binding.exSevenCalendar.notifyDateChanged(LocalDate.parse(it)) }
    }
}

//class TaskManagerFrag : BaseFragment(R.layout.fragment_task_manager), HasToolbar, HasBackButton {
//    override val titleRes: Int = R.string.example_7_title
//
//    override val toolbar: Toolbar
//        get() = binding.exSevenToolbar
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private var selectedDate = LocalDate.now()
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
//
//    private lateinit var binding: FragmentTaskManagerBinding
//    private lateinit var taskAdapter: TaskAdapter
//    private lateinit var viewModel: TaskViewModel
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentTaskManagerBinding.bind(view)
//        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
//        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
//            viewModel.getTasksByDate(date).observe(viewLifecycleOwner) { tasks ->
//                taskAdapter.submitList(tasks)
//            }
//        }
//        taskAdapter = TaskAdapter{
//            AlertDialog.Builder(requireContext())
//                .setMessage("Confirm Delete")
//                .setPositiveButton("Delete") { _, _ ->
//                    deleteEvent(it)
//                }
//                .setNegativeButton("Close", null)
//                .show()
//        }
//        binding.exThreeRv.apply {
//            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
//            adapter = taskAdapter
//            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
//        }
//
//        binding.fabShowDialog.setOnClickListener{
//            showAddTaskDialog()
//        }
//
//        @RequiresApi(Build.VERSION_CODES.O)
//        class DayViewContainer(view: View) : ViewContainer(view) {
//            val bind = ItemCalenderDayBinding.bind(view)
//            lateinit var day: WeekDay
//
//            init {
//                view.setOnClickListener {
////                    if (selectedDate != day.date) {
////                        val oldDate = selectedDate
////                        selectedDate = day.date
////                        binding.exSevenCalendar.notifyDateChanged(day.date)
////                        oldDate?.let { binding.exSevenCalendar.notifyDateChanged(it) }
////                    }
//
////                    if (viewModel.selectedDate.value != day.date.toString()) {
////                        viewModel.updateSelectedDate(day.date.toString())
////                        binding.exSevenCalendar.notifyDateChanged(day.date)
////                    }
//
//                    val newDate = day.date.toString()
//                    val oldDate = viewModel.selectedDate.value
//
//                    if (oldDate != newDate) {
//                        viewModel.updateSelectedDate(newDate) // Update selected date in ViewModel
//                        binding.exSevenCalendar.notifyDateChanged(day.date) // Refresh clicked date
//                        oldDate?.let { binding.exSevenCalendar.notifyDateChanged(LocalDate.parse(it)) } // Refresh previous date
//                    }
//                }
//            }
//
//            fun bind(day: WeekDay) {
//                this.day = day
//                bind.exSevenDateText.text = dateFormatter.format(day.date)
//                bind.exSevenDateText.setTextColor(view.context.getColor(R.color.black))
//                bind.exSevenDayText.text = day.date.dayOfWeek.displayText()
//                bind.exSevenDayText.setTextColor(view.context.getColor(R.color.black))
//
//                val isSelected = day.date.toString() == viewModel.selectedDate.value
//              //  bind.exSevenSelectedView.isVisible = isSelected
//
//                val colorRes = if (isSelected) {
//                    R.color.TASKcOL2// Selected date color
//                } else {
//                    R.color.white // Default color
//                }
//
//                bind.TaskDayCol.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, colorRes))
//
//
////                bind.TaskDayCol.setCardBackgroundColor(ContextCompat.getColor(view.context, colorRes))
////                bind.TaskDayCol.invalidate()
//
//                //bind.exSevenDateText.setTextColor(view.context.getColor(colorRes))
//                //bind.exSevenSelectedView.isVisible = day.date == selectedDate
//
//               // bind.exSevenSelectedView.isVisible = day.date.toString() == viewModel.selectedDate.value
//
//            }
//        }
//
//        binding.exSevenCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun create(view: View) = DayViewContainer(view)
//            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
//        }
//
//        binding.exSevenCalendar.weekScrollListener = { weekDays ->
//            binding.exSevenToolbar.title = getWeekPageTitle(weekDays)
//            binding.exSevenToolbar.setTitleTextColor(view.context.getColor(R.color.black))
//            //binding.exSevenToolbar.navigationIcon.setColorFilter()
//        }
//
//        val currentMonth = YearMonth.now()
//        binding.exSevenCalendar.setup(
//            currentMonth.minusMonths(5).atStartOfMonth(),
//            currentMonth.plusMonths(5).atEndOfMonth(),
//            firstDayOfWeekFromLocale(),
//        )
//        binding.exSevenCalendar.scrollToDate(LocalDate.now())
//    }
//
//    private fun deleteEvent(task: Task) {
//        val date = task.date
//        viewModel.deleteTask(task)
//    }
//
//        @RequiresApi(Build.VERSION_CODES.O)
//    private fun showAddTaskDialog() {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
//
//        val dialog = AlertDialog.Builder(requireContext())
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
//              //  val selectedDate = LocalDate.now().toString()
//                val selectedDate1 = viewModel.selectedDate.value ?: LocalDate.now().toString()
//                val task = Task(title = taskTitle, description = "", date = selectedDate1)
//               // viewModel.addTASK(task)
//
////                viewModel.getTasksByDate(selectedDate.toString()).observe(viewLifecycleOwner) { tasks ->
////                    taskAdapter.submitList(tasks)
////                }
//
//                viewModel.addTASK(task).invokeOnCompletion {
//                    requireActivity().runOnUiThread {
//                        viewModel.getTasksByDate(selectedDate.toString()).observe(viewLifecycleOwner) { tasks ->
//                            taskAdapter.submitList(tasks)
//                        }
//                        binding.exSevenCalendar.notifyDateChanged(LocalDate.parse(selectedDate.toString())) // Ensure selected date is updated
//                        //binding.exSevenCalendar.notifyDateChanged(LocalDate.now()) // Refresh clicked date
//                    }
//                }
//                dialog.dismiss()
//            } else {
//                taskTitleInput.error = "Task title cannot be empty"
//            }
//        }
//
//        dialog.show()
//    }
//}
