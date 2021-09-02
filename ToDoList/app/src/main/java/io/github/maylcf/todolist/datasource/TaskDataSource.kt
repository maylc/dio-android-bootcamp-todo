package io.github.maylcf.todolist.datasource

import io.github.maylcf.todolist.model.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()

    fun getList() = list.toList()

    // Scenario: Adicione 3 items [1,2,3]
    // remove first and last one >> [2]
    // adicione a new one -> issue! Id will be 2

    fun insertTask(task: Task) {
        if (task.id == 0) {
            list.add(task.copy(id = list.size + 1))
        } else {
            list.remove(task)
            list.add(task)
        }
    }

    fun findById(taskId: Int): Task? {
        return list.find { it.id == taskId }
    }

    fun deleteTask(task: Task) {
        list.remove(task)
    }
}