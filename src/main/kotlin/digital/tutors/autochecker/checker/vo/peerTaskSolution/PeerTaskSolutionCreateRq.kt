package digital.tutors.autochecker.checker.vo.peerTaskSolution

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskVO
import digital.tutors.autochecker.core.entity.EntityRefRq
import java.util.*

data class PeerTaskSolutionCreateRq(
        var id: String?,
        var taskId: EntityRefRq?,
        var userId: EntityRefRq?,
        var language: String?,
        var sourceCode: String?,
        var attempt: Int,
        var status: PeerTaskResultsStatus,
        var createdAt: Date
)