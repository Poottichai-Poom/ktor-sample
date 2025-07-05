package com.example

import kotlinx.serialization.Serializable

@Serializable
class Task(val id: Int, val content: String, val isDone: Boolean)

@Serializable
data class TaskRequest(val content: String,val isDone: Boolean)

object TaskRepository{
    private val tasks = mutableListOf<Task>(
        Task(id = 1, content = "Learn Ktor", isDone = true),
        Task(id = 2, content = "Build a REST API", isDone = false),
        Task(id = 3, content = "Write Unit Tests", isDone = false)
    )

    fun getAllTasks(): List<Task> = tasks.toList()
    fun getById(id: Int): Task? = tasks.find { it.id == id }
    fun addTask(id: Int, content: String, isDone: Boolean) {
        tasks.add(Task(id, content, isDone))
    }
    fun updateTask(id: Int, content: String, isDone: Boolean) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            tasks[index] = Task(id, content, isDone)
        }
    }
    fun deleteTask(id: Int) {
        tasks.removeIf { it.id == id }
    }
}





