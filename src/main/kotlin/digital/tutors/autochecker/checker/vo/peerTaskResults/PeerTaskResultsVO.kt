package digital.tutors.autochecker.checker.vo.peerTaskResults

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.PeerTaskResults
import digital.tutors.autochecker.checker.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewVO
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskVO
import java.util.*

data class PeerTaskResultsVO(
        var id: String?,
        var taskId: PeerTaskVO?,
        var studentId: UserVO?,
        var receivedReviews: List<PeerReviewVO>?,
        var postedReviews: List<PeerReviewVO>?,
        var grade: Int?,
        var completed: Boolean,
        var status: PeerTaskResultsStatus,
        var createdAt: Date,
        var updatedAt: Date
){
    companion object {
        fun fromData(taskResults: PeerTaskResults, userId: String?): PeerTaskResultsVO =
                PeerTaskResultsVO(
                        taskResults.id,
                        taskResults.taskId?.let { PeerTaskVO.fromData(it, userId, taskResults.completed) },
                        taskResults.studentId?.let { UserVO.fromData(it, null) },
                        taskResults.receivedReviews?.map { PeerReviewVO.fromData(it, userId, taskResults.completed)},
                        taskResults.receivedReviews?.map { PeerReviewVO.fromData (it, userId, taskResults.completed)},
                        taskResults.grade,
                        taskResults.completed,
                        taskResults.status,
                        taskResults.createdAt,
                        taskResults.updatedAt
                )
    }
}