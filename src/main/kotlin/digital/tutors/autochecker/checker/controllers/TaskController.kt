package digital.tutors.autochecker.checker.controllers

import digital.tutors.autochecker.checker.services.TaskService
import digital.tutors.autochecker.checker.vo.task.TaskAdminVO
import digital.tutors.autochecker.checker.vo.task.TaskCreateRq
import digital.tutors.autochecker.checker.vo.task.TaskUpdateRq
import digital.tutors.autochecker.checker.vo.task.TaskVO
import digital.tutors.autochecker.core.controller.BaseController
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping
class TaskController : BaseController() {

    @Autowired
    lateinit var taskService: TaskService

    @GetMapping("/tasks")
    fun getTasks(@RequestParam page: Int): ResponseEntity<Page<TaskAdminVO>> = processServiceExceptions {
        try {
            val pageRequest = PageRequest.of(page, 10);
            ResponseEntity.ok(taskService.getTasks(pageRequest))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tasks Not Found", ex)
        }
    }

    @GetMapping("/author/{id}/tasks")
    fun getTasksByAuthorId(@PathVariable id: String): ResponseEntity<List<TaskVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(taskService.getTasksByAuthorId(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tasks Not Found", ex)
        }
    }

    @GetMapping("/topic/{id}/tasks")
    fun getTasksByTopicId(@PathVariable id: String): ResponseEntity<List<TaskVO>> = processServiceExceptions {
        /*
            TODO: Проверка на получение тасков может быть от студента или препода
         */

        try {
            ResponseEntity.ok(taskService.getTasksByTopicId(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tasks Not Found", ex)
        }
    }

    @GetMapping("/task/{id}")
    fun getTaskById(@PathVariable id: String): ResponseEntity<TaskVO> = processServiceExceptions {
        try {
            ResponseEntity.ok(taskService.getTaskByIdOrThrow(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found", ex)
        }
    }

    @GetMapping("/task/{id}/admin")
    fun getAdminTaskById(@PathVariable id: String): ResponseEntity<TaskAdminVO> = processServiceExceptions {
        try {
            ResponseEntity.ok(taskService.getAdminTaskByIdOrThrow(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found", ex)
        }
    }

    @PostMapping("/task")
    fun createTask(@RequestBody taskCreateRq: TaskCreateRq): ResponseEntity<TaskVO> = processServiceExceptions {
        ResponseEntity.ok(taskService.createTask(taskCreateRq))
    }

    @PutMapping("/task/{id}")
    fun updateTask(@PathVariable id: String, @RequestBody taskUpdateRq: TaskUpdateRq): ResponseEntity<TaskVO> = processServiceExceptions {
        ResponseEntity.ok(taskService.updateTask(id, taskUpdateRq))
    }

    @DeleteMapping("/task/{id}")
    fun deleteTask(@PathVariable id: String): ResponseEntity<*> = processServiceExceptions {
        ResponseEntity.ok(taskService.delete(id))
    }

}
