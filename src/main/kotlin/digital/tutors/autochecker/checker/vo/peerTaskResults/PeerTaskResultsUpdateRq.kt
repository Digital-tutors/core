package digital.tutors.autochecker.checker.vo.peerTaskResults

import digital.tutors.autochecker.checker.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.core.entity.EntityRefRq
import java.util.*

data class PeerTaskResultsUpdateRq(
        var receivedReviews: List<EntityRefRq>?,
        var postedReviews: List<EntityRefRq>?,
        var grade: Int?,
        var completed: Boolean,
        var status: PeerTaskResultsStatus,
        var updatedAt: Date
)