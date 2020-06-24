package digital.tutors.autochecker.reviewer.vo.peerTaskResults

import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.core.entity.EntityRefRq
import java.util.*

data class PeerTaskResultsCreateRq(
        var taskId: EntityRefRq? = null,
        var studentId: EntityRefRq? = null
)