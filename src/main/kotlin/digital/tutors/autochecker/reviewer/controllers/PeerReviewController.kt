package digital.tutors.autochecker.reviewer.controllers

import digital.tutors.autochecker.reviewer.services.PeerReviewService
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewCreateRq
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewUpdateRq
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewVO
import digital.tutors.autochecker.core.controller.BaseController
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping
class PeerReviewController : BaseController() {

    @Autowired
    lateinit var peerReviewService: PeerReviewService

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
}