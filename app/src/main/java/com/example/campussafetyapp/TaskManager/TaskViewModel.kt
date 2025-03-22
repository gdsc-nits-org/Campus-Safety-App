package com.example.campussafetyapp.TaskManager

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(application: Application) :AndroidViewModel(application){

    private val db =  TaskDatabase.getDatabase(application).taskDao()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _selectedDate = MutableLiveData(LocalDate.now().toString())
    @RequiresApi(Build.VERSION_CODES.O)
    val selectedDate: LiveData<String> = _selectedDate

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun getTasksByDate(date: String): LiveData<List<Task>> = db.getTasksByDate(date)


    //fun getTasks(date :String):LiveData<List<Task>> = db.getTasksByDate(date)

    fun addTASK(task :Task) = viewModelScope.launch {
        db.insertTask(task)
    }

    fun deleteTask(task:Task) = viewModelScope.launch { db.deleteTask(task) }
}