package digital.tutors.autochecker.reviewer.vo.peerTaskResults

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.reviewer.entities.PeerTaskResults
import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewVO
import digital.tutors.autochecker.reviewer.vo.peerTask.PeerTaskVO
import java.util.*

data class PeerTaskResultsVO(
        var id: String?,
        var taskId: PeerTaskVO?,
        var studentId: UserVO?,
        var receivedReviews: List<PeerReviewVO>?,
        var postedReviews: List<PeerReviewVO>?,
        var grade: Int?,
        var completed: Boolean,
        var status: PeerTaskResultsStatus
){
    companion object {
        fun fromData(taskResults: PeerTaskResults, userId: String?, isCompleted: Boolean): PeerTaskResultsVO =
                PeerTaskResultsVO(
                        taskResults.id,
                        taskResults.taskId?.let { PeerTaskVO.fromData(it, userId, isCompleted) },
                        taskResults.studentId?.let { UserVO.fromData(it, null) },
                        taskResults.receivedReviews?.map { PeerReviewVO.fromData(it, userId)},
                        taskResults.receivedReviews?.map { PeerReviewVO.fromData (it, userId)},
                        taskResults.grade,
                        taskResults.completed,
                        taskResults.status
                )
    }
}