package digital.tutors.autochecker.checker.vo.peerTaskResults

import digital.tutors.autochecker.checker.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.core.entity.EntityRefRq
import java.util.*

data class PeerTaskResultsCreateRq(
        var taskId: EntityRefRq?,
        var studentId: EntityRefRq?,
        var receivedReviews: List<EntityRefRq>?,
        var postedReviews: List<EntityRefRq>?,
        var grade: Int?,
        var completed: Boolean,
        var status: PeerTaskResultsStatus,
        var createdAt: Date,
        var updatedAt: Date
)