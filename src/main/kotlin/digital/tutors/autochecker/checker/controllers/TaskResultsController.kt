package digital.tutors.autochecker.checker.controllers

import digital.tutors.autochecker.checker.services.TaskResultsService
import digital.tutors.autochecker.checker.services.TaskService
import digital.tutors.autochecker.checker.vo.task.TaskCreateRq
import digital.tutors.autochecker.checker.vo.task.TaskUpdateRq
import digital.tutors.autochecker.checker.vo.task.TaskVO
import digital.tutors.autochecker.checker.vo.taskResults.TaskResultsCreateRq
import digital.tutors.autochecker.checker.vo.taskResults.TaskResultsVO
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
class TaskResultsController: BaseController() {

    @Autowired
    lateinit var taskResultsService: TaskResultsService

    @GetMapping("/decisions")
    fun getDecisions(@RequestParam page: Int): ResponseEntity<Page<TaskResultsVO>> = processServiceExceptions {
        try {
            val pageRequest = PageRequest.of(page,10);
            ResponseEntity.ok(taskResultsService.getTaskResults(pageRequest))
        }
        catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Decisions Not Found", ex)
        }
    }

    @GetMapping("/author/{id}/decisions")
    fun getTasksByAuthorId(@PathVariable id: String): ResponseEntity<List<TaskResultsVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(taskResultsService.getTaskResultsByAuthorId(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Decisions Not Found", ex)
        }
    }

    @GetMapping("/user/{user}/task/{task}/decisions")
    fun getTasksByUserAndTask(@PathVariable user: String, @PathVariable task: String): ResponseEntity<List<TaskResultsVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(taskResultsService.getTaskResultsByUserAndTask(user, task))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Decisions for user with topic Not Found", ex)
        }
    }

    @GetMapping("/decision/{id}")
    fun getDecisionById(@PathVariable id: String): ResponseEntity<TaskResultsVO> = processServiceExceptions {
        try {
            ResponseEntity.ok(taskResultsService.getTaskResultsByIdOrThrow(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Decision Not Found", ex)
        }
    }

    @PostMapping("/decision")
    fun saveDecision(@RequestBody taskResultsCreateRq: TaskResultsCreateRq): ResponseEntity<TaskResultsVO> = processServiceExceptions {
        ResponseEntity.ok(taskResultsService.saveTaskResults(taskResultsCreateRq))
    }

    @DeleteMapping("/decision/{id}")
    fun deleteDecision(@PathVariable id: String): ResponseEntity<*> = processServiceExceptions {
        ResponseEntity.ok(taskResultsService.delete(id))
    }

}
