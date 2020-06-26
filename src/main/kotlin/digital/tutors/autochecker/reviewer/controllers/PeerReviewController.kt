package digital.tutors.autochecker.reviewer.controllers

import digital.tutors.autochecker.checker.vo.task.TaskAdminVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.reviewer.services.PeerReviewService
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewCreateRq
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewUpdateRq
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewVO
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
class PeerReviewController : BaseController() {

    @Autowired
    lateinit var peerReviewService: PeerReviewService

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @PostMapping("/review")
    fun createPeerReview(@RequestBody peerReviewCreateRq: PeerReviewCreateRq): ResponseEntity<PeerReviewVO> = processServiceExceptions {
        ResponseEntity.ok(peerReviewService.createPeerReview(peerReviewCreateRq))
    }

    @PutMapping("/reviews/{id}")
    fun updatePeerReview(@PathVariable id: String, @RequestBody peerReviewUpdateRq: PeerReviewUpdateRq): ResponseEntity<PeerReviewVO> = processServiceExceptions {
        ResponseEntity.ok(peerReviewService.updatePeerReview(id, peerReviewUpdateRq))
    }

    @GetMapping("/reviews/expert/{id}")
    fun getPostedReviewsByUserId(@PathVariable id: String): ResponseEntity<List<PeerReviewVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerReviewService.getPeerReviewsByExpertId(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not Found", ex)
        }
    }

    @GetMapping("/reviews/student")
    fun getReceivedReviewsByUser(): ResponseEntity<List<PeerReviewVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerReviewService.getPeerReviewsByStudentId(authorizationService.currentUserIdOrDie()))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not Found", ex)
        }
    }

    @GetMapping("/reviews/student/{id}")
    fun getReceivedReviewsByUserId(@PathVariable id: String): ResponseEntity<List<PeerReviewVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerReviewService.getPeerReviewsByStudentId(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not Found", ex)
        }
    }

    @GetMapping("task/peer/{task}/reviews")
    fun getPeerReviewsByTaskId(@PathVariable task: String): ResponseEntity<List<PeerReviewVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerReviewService.getPeerReviewsByPeerTaskId(task))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not Found", ex)
        }
    }

    @GetMapping("/user/{userId}/task/peer/{taskId}")
    fun getPeerReviewsByStudentIdAndTaskId(@PathVariable userId: String, @PathVariable taskId: String): ResponseEntity<List<PeerReviewVO>> = processServiceExceptions{
        try {
            ResponseEntity.ok(peerReviewService.getPeerReviewsByStudentIdAndTaskId(userId, taskId))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not found", ex)
        }
    }

    @GetMapping("/task/peer/{taskId}/receivedReviews")
    fun getReceivedReviewsByUserAndTaskId(@PathVariable taskId: String, @RequestParam(defaultValue = "0") page: Int): ResponseEntity<Page<PeerReviewVO>> = processServiceExceptions{
        try {
            val pageRequest = PageRequest.of(page, 50)
            ResponseEntity.ok(peerReviewService.getPeerReviewsByUserIdAndTaskId(authorizationService.currentUserIdOrDie(), taskId, pageRequest))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not found", ex)
        }
    }

    @GetMapping("/reviews")
    fun getReviews(@RequestParam page: Int): ResponseEntity<Page<PeerReviewVO>> = processServiceExceptions {
        try {
            val pageRequest = PageRequest.of(page, 10);
            ResponseEntity.ok(peerReviewService.getPeerReviews(pageRequest))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not Found", ex)
        }
    }

    @GetMapping("/user/receivedReviews")
    fun getReceivedReviewsByUserPerPage(@RequestParam page: Int): ResponseEntity<Page<PeerReviewVO>> = processServiceExceptions {
        try {
            val pageRequest = PageRequest.of(page, 10);
            ResponseEntity.ok(peerReviewService.getAllReceivedReviewsByUserId(authorizationService.currentUserIdOrDie(), pageRequest))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not Found", ex)
        }
    }
}