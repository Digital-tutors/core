package digital.tutors.autochecker.reviewer.services

import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewCreateRq
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewUpdateRq
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewVO
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

interface PeerReviewService {

    @Throws(EntityNotFoundException::class)
    fun getPeerReviewsByExpertId(expertId: String): List<PeerReviewVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerReviewsByStudentId(studentId: String): List<PeerReviewVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerReviewsByPeerTaskId(taskId: String): List<PeerReviewVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerReviewsBySolutionId(solutionId: String): List<PeerReviewVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerReviewByIdOrThrow(id: String): PeerReviewVO

    @Throws(EntityNotFoundException::class)
    fun getPeerReviews(pageable: Pageable): Page<PeerReviewVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerReviewsByStudentIdAndTaskId(studentId: String, peerTaskId: String): List<PeerReviewVO>

    @Throws(EntityNotFoundException::class)
    fun createPeerReview(peerReviewCreateRq: PeerReviewCreateRq): PeerReviewVO

    @Throws(EntityNotFoundException::class)
    fun updatePeerReview(id: String, peerReviewUpdateRq: PeerReviewUpdateRq): PeerReviewVO

    @Throws(EntityNotFoundException::class)
    fun getPeerReviewsByUserIdAndTaskId(userId: String, peerTaskId: String, pageRequest: PageRequest): Page<PeerReviewVO>

    @Throws(EntityNotFoundException::class)
    fun getAllReceivedReviewsByUserId(userId: String, pageRequest: PageRequest): Page<PeerReviewVO>
}