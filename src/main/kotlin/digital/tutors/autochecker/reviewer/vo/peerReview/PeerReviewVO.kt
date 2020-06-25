package digital.tutors.autochecker.reviewer.vo.peerReview

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.reviewer.entities.PeerReview
import digital.tutors.autochecker.reviewer.vo.peerTask.PeerTaskVO
import digital.tutors.autochecker.reviewer.vo.peerTaskSolution.PeerTaskSolutionVO
import java.util.*

data class PeerReviewVO (
        var id: String?,
        var taskId: PeerTaskVO?,
        var studentId: UserVO?,
        var expertId: UserVO?,
        var solutionId: PeerTaskSolutionVO?,
        var gradesPerCriterions: List<Int>?,
        var argumentsPerCriterions: List<String>?,
        var summaryMessagePerSolution: String?,
        var grade: Double
) {
    companion object {
        fun fromData(peerReview: PeerReview, userId: String?): PeerReviewVO =
                PeerReviewVO(
                        peerReview.id,
                        peerReview.taskId?.let { PeerTaskVO.fromData(it, userId, it.isCompleted) },
                        peerReview.studentId?.let { UserVO.fromData(it, null) },
                        peerReview.expertId?.let { UserVO.fromData(it, null) },
                        peerReview.solutionId?.let { PeerTaskSolutionVO.fromData(it, null) },
                        peerReview.gradesPerCriterions,
                        peerReview.argumentsPerCriterions,
                        peerReview.summaryMessagePerSolution,
                        peerReview.grade
                )
    }
}