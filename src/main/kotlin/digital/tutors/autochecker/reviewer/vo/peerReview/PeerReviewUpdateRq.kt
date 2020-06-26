package digital.tutors.autochecker.reviewer.vo.peerReview

import java.util.*

data class PeerReviewUpdateRq(
        var gradesPerCriterions: List<Double>?,
        var argumentsPerCriterions: List<String>?,
        var summaryMessagePerSolution: String?,
        var grade: Double?
)