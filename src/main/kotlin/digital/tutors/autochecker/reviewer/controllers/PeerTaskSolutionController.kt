package digital.tutors.autochecker.reviewer.controllers

import digital.tutors.autochecker.reviewer.services.PeerTaskSolutionService
import digital.tutors.autochecker.reviewer.vo.peerTaskSolution.PeerTaskSolutionCreateRq
import digital.tutors.autochecker.reviewer.vo.peerTaskSolution.PeerTaskSolutionVO
import digital.tutors.autochecker.core.auth.AuthorizationService
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
class PeerTaskSolutionController: BaseController() {

    @Autowired
    lateinit var peerTaskSolutionService: PeerTaskSolutionService

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @GetMapping("/solutions/peer")
    fun getPeerDecisions(@RequestParam page: Int): ResponseEntity<Page<PeerTaskSolutionVO>> = processServiceExceptions {
        try {
            val pageRequest = PageRequest.of(page,10);
            ResponseEntity.ok(peerTaskSolutionService.getPeerTaskSolutions(pageRequest))
        }  catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Decisions Not Found", ex)
        }
    }

    @GetMapping("/author/{id}/solution/peer")
    fun getPeerTaskSolutionsByAuthorId(@PathVariable id: String): ResponseEntity<List<PeerTaskSolutionVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerTaskSolutionService.getPeerTaskSolutionsByUser(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Decisions Not Found", ex)
        }
    }

    @GetMapping("/task/peer/{peerTask}/solutions")
    fun getPeerTaskSolutionsByUserAndTask(@PathVariable peerTask: String): ResponseEntity<List<PeerTaskSolutionVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerTaskSolutionService.getPeerTaskSolutionsByUserAndTask(authorizationService.currentUserIdOrDie(), peerTask))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Decisions for user with topic Not Found", ex)
        }
    }

    @PostMapping("/solution/peer")
    fun savePeerTaskSolution(@RequestBody peerTaskSolutionCreateRq: PeerTaskSolutionCreateRq): ResponseEntity<PeerTaskSolutionVO> = processServiceExceptions {
            ResponseEntity.ok(peerTaskSolutionService.savePeerTaskSolution(peerTaskSolutionCreateRq))
    }
}