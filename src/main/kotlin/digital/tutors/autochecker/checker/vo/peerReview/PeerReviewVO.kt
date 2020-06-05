package digital.tutors.autochecker.checker.vo.peerReview

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.PeerReview
import digital.tutors.autochecker.checker.entities.PeerTask
import digital.tutors.autochecker.checker.entities.PeerTaskSolution
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskVO
import digital.tutors.autochecker.checker.vo.peerTaskSolution.PeerTaskSolutionVO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
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
        var createdAt: Date,
        var updatedAt: Date
) {
    companion object {
        fun fromData(peerReview: PeerReview, userId: String?, isCompleted: Boolean): PeerReviewVO =
                PeerReviewVO(
                        peerReview.id,
                        peerReview.taskId?.let { PeerTaskVO.fromData(it, userId, isCompleted) },
                        peerReview.studentId?.let { UserVO.fromData(it, null) },
                        peerReview.expertId?.let { UserVO.fromData (it, null) },
                        peerReview.solutionId?.let { PeerTaskSolutionVO.fromData(it, null, isCompleted) },
                        peerReview.gradesPerCriterions,
                        peerReview.argumentsPerCriterions,
                        peerReview.summaryMessagePerSolution,
                        peerReview.createdAt,
                        peerReview.updatedAt
                )
    }
}