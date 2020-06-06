package digital.tutors.autochecker.checker.controllers

import digital.tutors.autochecker.checker.services.PeerReviewService
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewCreateRq
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewUpdateRq
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewVO
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskCreateRq
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskVO
import digital.tutors.autochecker.checker.vo.task.TaskUpdateRq
import digital.tutors.autochecker.checker.vo.task.TaskVO
import digital.tutors.autochecker.core.controller.BaseController
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping
class PeerReviewController: BaseController() {

    @Autowired
    lateinit var peerReviewService: PeerReviewService

    @PostMapping("/task/peer/create")
    fun createPeerReview(@RequestBody peerReviewCreateRq: PeerReviewCreateRq): ResponseEntity<PeerReviewVO> = processServiceExceptions {
        ResponseEntity.ok(peerReviewService.createPeerReview(peerReviewCreateRq))
    }

    @PutMapping("/task/peer/update/{id}")
    fun updatePeerReview(@PathVariable id: String, @RequestBody peerReviewUpdateRq: PeerReviewUpdateRq): ResponseEntity<PeerReviewVO> = processServiceExceptions {
        ResponseEntity.ok(peerReviewService.updatePeerReview(id, peerReviewUpdateRq))
    }

    @GetMapping("/task/peer/expert/{id}")
    fun getPostedReviewsByUserId(@PathVariable id: String):  ResponseEntity<List<PeerReviewVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerReviewService.getPeerReviewsByExpertId(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not Found", ex)
        }
    }

    @GetMapping("/task/peer/student/{id}")
    fun getReceivedReviewsByUserId(@PathVariable id: String):  ResponseEntity<List<PeerReviewVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerReviewService.getPeerReviewsByStudentId(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not Found", ex)
        }
    }

    @GetMapping("/task/peer/get")
    fun getPeerReviewsByTaskId(@PathVariable id: String):  ResponseEntity<List<PeerReviewVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(peerReviewService.getPeerReviewsByStudentId(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Peer reviews not Found", ex)
        }
    }
}