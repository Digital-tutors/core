package digital.tutors.autochecker.reviewer.vo.peerReview

import java.util.*

data class PeerReviewUpdateRq(
        var gradesPerCriterions: List<Int>?,
        var argumentsPerCriterions: List<String>?,
        var summaryMessagePerSolution: String?
)