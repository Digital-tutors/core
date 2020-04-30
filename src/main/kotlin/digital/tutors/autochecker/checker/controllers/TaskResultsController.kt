package digital.tutors.autochecker.checker.controllers

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
    lateinit var taskService: TaskService

    @GetMapping("/decisions")
    fun getDecisions(@RequestParam page: Int): ResponseEntity<Page<TaskResultsVO>> = processServiceExceptions {
//        try {
//            val pageRequest = PageRequest.of(page,10);
//            ResponseEntity.ok(taskService.getTasks(pageRequest))
//        }
//        catch (ex: EntityNotFoundException) {
//            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Decisions Not Found", ex)
//        }

        TODO()
    }

    @GetMapping("/decision/{id}")
    fun getDecisionById(@PathVariable id: String): ResponseEntity<TaskResultsVO> = processServiceExceptions {
//        try {
//            ResponseEntity.ok(taskService.getTaskByIdOrThrow(id))
//        } catch (ex: EntityNotFoundException) {
//            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Decision Not Found", ex)
//        }

        TODO()
    }

    @PostMapping("/decision")
    fun createDecision(@RequestBody taskCreateRq: TaskCreateRq): ResponseEntity<TaskResultsCreateRq> = processServiceExceptions {
        ResponseEntity.ok(taskService.createTask(taskCreateRq))
        TODO()
    }

    @DeleteMapping("/decision/{id}")
    fun deleteDecision(@PathVariable id: String): ResponseEntity<*> = processServiceExceptions {
        ResponseEntity.ok(taskService.delete(id))
    }

}
