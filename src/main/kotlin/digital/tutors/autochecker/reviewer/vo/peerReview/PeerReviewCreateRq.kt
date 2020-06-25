package digital.tutors.autochecker.reviewer.vo.peerReview

import digital.tutors.autochecker.core.entity.EntityRefRq
import java.util.*

data class PeerReviewCreateRq(
        var taskId: EntityRefRq? = null,
        var studentId: EntityRefRq? = null,
        var expertId: EntityRefRq? = null,
        var solutionId: EntityRefRq? = null,
        var gradesPerCriterions: List<Int>?,
        var argumentsPerCriterions: List<String>?,
        var summaryMessagePerSolution: String?,
        var grade: Double?
)