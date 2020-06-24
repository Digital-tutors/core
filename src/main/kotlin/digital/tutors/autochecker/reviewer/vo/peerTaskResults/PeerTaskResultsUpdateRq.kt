package digital.tutors.autochecker.reviewer.vo.peerTaskResults

import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.core.entity.EntityRefRq
import java.util.*

data class PeerTaskResultsUpdateRq(
        var grade: Int,
        var completed: Boolean,
        var status: PeerTaskResultsStatus
)