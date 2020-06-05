package digital.tutors.autochecker.checker.vo.peerReview

import java.util.*

data class PeerReviewUpdateRq(
        var gradesPerCriterions: List<Int>? = null,
        var argumentsPerCriterions: List<String>? = null,
        var summaryMessagePerSolution: String?,
        var updatedAt: Date
)