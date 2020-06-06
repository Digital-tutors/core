package digital.tutors.autochecker.checker.controllers

import digital.tutors.autochecker.checker.services.PeerTaskService
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskAdminVO
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskCreateRq
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskUpdateRq
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskVO
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
class PeerTaskController: BaseController() {

    @Autowired
    lateinit var peerTaskService: PeerTaskService

    @GetMapping("/tasks")
    fun getPeerTasks(@RequestParam page: Int): ResponseEntity<Page<PeerTaskAdminVO>> = processServiceExceptions {
        try {
            val pageRequest = PageRequest.of(page, 10)
            ResponseEntity.ok(peerTaskService.getPeerTasks(pageRequest))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tasks Not Found", ex)
        }
    }

    @GetMapping("/author/{id}/tasks")
    fun getTasksByAuthorId(@PathVariable id: String): ResponseEntity<List<PeerTaskVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerTaskService.getPeerTasksByAuthorId(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tasks Not Found", ex)
        }
    }

    @GetMapping("/topic/{id}/tasks")
    fun getTasksByTopicId(@PathVariable id: String): ResponseEntity<List<PeerTaskVO>> = processServiceExceptions {
        /*
            TODO: Проверка на получение тасков может быть от студента или препода
         */

        try {
            ResponseEntity.ok(peerTaskService.getPeerTasksByTopicId(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tasks Not Found", ex)
        }
    }

    @GetMapping("/task/{id}")
    fun getTaskById(@PathVariable id: String): ResponseEntity<PeerTaskVO> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerTaskService.getPeerTaskByIdOrThrow(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found", ex)
        }
    }

    @GetMapping("/task/{id}/admin")
    fun getAdminTaskById(@PathVariable id: String): ResponseEntity<PeerTaskAdminVO> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerTaskService.getAdminPeerTaskByIdOrThrow(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found", ex)
        }
    }

    @PostMapping("/task/peer")
    fun createTask(@RequestBody peerTaskCreateRq: PeerTaskCreateRq): ResponseEntity<PeerTaskVO> = processServiceExceptions {
        ResponseEntity.ok(peerTaskService.createPeerTask(peerTaskCreateRq))
    }

    @PutMapping("/task/peer/{id}")
    fun updateTask(@PathVariable id: String, @RequestBody peerTaskUpdateRq: PeerTaskUpdateRq): ResponseEntity<PeerTaskVO> = processServiceExceptions {
        ResponseEntity.ok(peerTaskService.updatePeerTask(id, peerTaskUpdateRq))
    }

    @DeleteMapping("/task/{id}")
    fun deleteTask(@PathVariable id: String): ResponseEntity<*> = processServiceExceptions {
        ResponseEntity.ok(peerTaskService.delete(id))
    }
}