package digital.tutors.autochecker.reviewer.vo.peerTaskSolution

import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.core.entity.EntityRefRq
import java.util.*

data class PeerTaskSolutionCreateRq(
        var taskId: EntityRefRq? = null,
        var userId: EntityRefRq? = null,
        var language: String?,
        var sourceCode: String?
)