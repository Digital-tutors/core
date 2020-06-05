package digital.tutors.autochecker.checker.vo.peerReview

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskVO
import digital.tutors.autochecker.core.entity.EntityRefRq
import java.util.*

data class PeerReviewCreateRq(
        var taskId: EntityRefRq? = null,
        var studentId: EntityRefRq? = null,
        var expertId: EntityRefRq? = null,
        var solutionId: EntityRefRq? = null,
        var gradesPerCriterions: List<Int>? = null,
        var argumentsPerCriterions: List<String>? = null,
        var summaryMessagePerSolution: String?,
        var createdAt: Date,
        var updatedAt: Date
)