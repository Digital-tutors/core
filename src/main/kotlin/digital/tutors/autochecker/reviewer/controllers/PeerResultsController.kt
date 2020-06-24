package digital.tutors.autochecker.reviewer.controllers

import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.reviewer.services.PeerTaskResultsService
import digital.tutors.autochecker.core.controller.BaseController
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsCreateRq
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping
class PeerResultsController: BaseController() {

    @Autowired
    lateinit var peerTaskResultsService: PeerTaskResultsService

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @GetMapping("/decisions/peer")
    fun getPeerDecisions(@RequestParam page: Int): ResponseEntity<Page<PeerTaskResultsVO>> = processServiceExceptions {
        try {
            val pageRequest = PageRequest.of(page,10);
            ResponseEntity.ok(peerTaskResultsService.getPeerTaskResults(pageRequest))
        }
        catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Results Not Found", ex)
        }
    }

    @GetMapping("/author/{id}/decisions/peer")
    fun getPeerTaskResultsByAuthorId(@PathVariable id: String): ResponseEntity<List<PeerTaskResultsVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerTaskResultsService.getPeerTaskResultsByUser(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Results Not Found", ex)
        }
    }

    @GetMapping("/task/{task}/decisions/peer")
    fun getPeerTaskResultsByUserAndTask(@PathVariable task: String): ResponseEntity<List<PeerTaskResultsVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerTaskResultsService.getPeerTaskResultsByUserAndTask(authorizationService.currentUserIdOrDie(), task))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Results for user with topic Not Found", ex)
        }
    }

    @GetMapping("/task/{task}/decision/peer/{user}")
    fun getPeerTaskResultByUserAndTask(@PathVariable task: String, @PathVariable user: String): ResponseEntity<PeerTaskResultsVO> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerTaskResultsService.getPeerTaskResultByPeerTaskIdAndUser(task, authorizationService.currentUserIdOrDie()))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Results for user with topic Not Found", ex)
        }
    }

    @GetMapping("/user/decisions/peer")
    fun getPeerDecisionsByUser(): ResponseEntity<List<PeerTaskResultsVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerTaskResultsService.getPeerTaskResultsByUser(authorizationService.currentUserIdOrDie()))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Results for user with topic Not Found", ex)
        }
    }

    @GetMapping("/decision/peer/{id}")
    fun getPeerDecisionById(@PathVariable id: String): ResponseEntity<PeerTaskResultsVO> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerTaskResultsService.getPeerTaskResultByIdOrThrow(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Results Not Found", ex)
        }
    }

    @PostMapping("/decision/peer")
    fun savePeerDecision(@RequestBody taskResultsCreateRq: PeerTaskResultsCreateRq): ResponseEntity<PeerTaskResultsVO> = processServiceExceptions {
        ResponseEntity.ok(peerTaskResultsService.createPeerTaskResult(taskResultsCreateRq))
    }

}