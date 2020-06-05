package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.checker.services.PeerReviewService
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewCreateRq
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewUpdateRq
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class PeerReviewServiceImpl: PeerReviewService {
    override fun getPeerReviewsByExpertId(expertId: String): List<PeerReviewVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerReviewsByStudentId(studentId: String): List<PeerReviewVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerReviewsByPeerTaskId(taskId: String): List<PeerReviewVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerReviewsBySolutionId(solutionId: String): List<PeerReviewVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerReviewByIdOrThrow(id: String): PeerReviewVO {
        TODO("Not yet implemented")
    }

    override fun getPeerReviews(pageable: Pageable): Page<PeerReviewVO> {
        TODO("Not yet implemented")
    }

    override fun createPeerReview(peerReviewCreateRq: PeerReviewCreateRq): PeerReviewVO {
        TODO("Not yet implemented")
    }

    override fun updatePeerReview(id: String, peerReviewUpdateRq: PeerReviewUpdateRq): PeerReviewVO {
        TODO("Not yet implemented")
    }

    override fun delete(id: String) {
        TODO("Not yet implemented")
    }
}