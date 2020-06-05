package digital.tutors.autochecker.checker.services

import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewCreateRq
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewUpdateRq
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewVO
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.data.domain.Page
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
    fun createPeerReview(peerReviewCreateRq: PeerReviewCreateRq): PeerReviewVO

    @Throws(EntityNotFoundException::class)
    fun updatePeerReview(id: String, peerReviewUpdateRq: PeerReviewUpdateRq): PeerReviewVO

    @Throws(EntityNotFoundException::class)
    fun delete(id: String)
}