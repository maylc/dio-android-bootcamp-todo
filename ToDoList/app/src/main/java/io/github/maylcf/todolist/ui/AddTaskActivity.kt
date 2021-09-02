package io.github.maylcf.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.github.maylcf.todolist.databinding.ActivityAddTaskBinding
import io.github.maylcf.todolist.datasource.TaskDataSource
import io.github.maylcf.todolist.extensions.format
import io.github.maylcf.todolist.extensions.text
import io.github.maylcf.todolist.model.Task
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddTaskBinding

    companion object {
        const val TASK_ID = "task_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilHour.text = it.hour
                binding.tilDate.text = it.date
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                Log.d("Mayara", Date(it).format())
                binding.tilDate.text = Date(it + getDateOffset()).format()
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                binding.tilHour.text = getDisplayTime(timePicker.minute, timePicker.hour)
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.btnNewTask.setOnClickListener {
            val task = Task(
                title = binding.tilTitle.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )

            TaskDataSource.insertTask(task)

            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun getDateOffset(): Int {
        val timeZone = TimeZone.getDefault()
        return timeZone.getOffset(Date().time) * -1
    }

    private fun getDisplayTime(minute: Int, hour: Int): String {
        val formattedMinute = if (minute in 0..9) "0${minute}" else minute
        val formattedHour = if (minute in 0..9) "0${hour}" else hour

        return "${formattedHour}:${formattedMinute}"
    }
}