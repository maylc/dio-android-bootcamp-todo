package io.github.maylcf.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import io.github.maylcf.todolist.adapter.TaskListAdapter
import io.github.maylcf.todolist.databinding.ActivityMainBinding
import io.github.maylcf.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            resultLauncher.launch(intent)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            resultLauncher.launch(intent)
        }
    }

    var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            updateList()
        }
    }

    private fun updateList() {
        val list = TaskDataSource.getList()

        binding.emptyLayout.emptyStateParent.visibility = if (list.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }

        adapter.submitList(list)
    }
}