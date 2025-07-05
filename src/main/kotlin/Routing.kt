package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.routes() {
    routing {
        // GET /tasks
        get("/tasks") {
            val allTasks = TaskRepository.getAllTasks()
            if (allTasks.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, allTasks)
            } else {
                call.respond(HttpStatusCode.NotFound, "No tasks found")
            }
        }

        // GET /tasks/{id}
        get("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@get
            }

            val task = TaskRepository.getById(id)
            if (task != null) {
                call.respond(HttpStatusCode.OK, task)
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        }

        // POST /tasks
        post("/tasks") {
            val request = call.receive<TaskRequest>()
            val newId = (TaskRepository.getAllTasks().maxOfOrNull { it.id } ?: 0) + 1
            TaskRepository.addTask(newId, request.content, request.isDone)
            val createdTask = TaskRepository.getById(newId)
            call.respond(HttpStatusCode.Created, createdTask!!)
        }

        // PUT /tasks/{id}
        put("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@put
            }

            val existing = TaskRepository.getById(id)
            if (existing == null) {
                call.respond(HttpStatusCode.NotFound, "Task not found")
                return@put
            }

            val updateRequest = call.receive<TaskRequest>()
            TaskRepository.updateTask(id, updateRequest.content, updateRequest.isDone)
            val updatedTask = TaskRepository.getById(id)
            call.respond(HttpStatusCode.OK, updatedTask!!)
        }

        // DELETE /tasks/{id}
        delete("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@delete
            }

            val existing = TaskRepository.getById(id)
            if (existing == null) {
                call.respond(HttpStatusCode.NotFound, "Task not found")
                return@delete
            }

            TaskRepository.deleteTask(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
