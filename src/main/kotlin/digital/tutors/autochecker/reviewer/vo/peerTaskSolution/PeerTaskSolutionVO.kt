package digital.tutors.autochecker.reviewer.vo.peerTaskSolution

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.reviewer.entities.PeerTaskSolution
import digital.tutors.autochecker.reviewer.vo.peerTask.PeerTaskVO
import java.util.*

data class PeerTaskSolutionVO(
        var id: String?,
        var taskId: PeerTaskVO?,
        var userId: UserVO?,
        var language: String?,
        var sourceCode: String?,
        var attempt: Int,
        var status: PeerTaskResultsStatus
){
    companion object {
        fun fromData(taskSolution: PeerTaskSolution, userId: String?) : PeerTaskSolutionVO =
                PeerTaskSolutionVO(
                        taskSolution.id,
                        taskSolution.taskId?.let { PeerTaskVO.fromData(it, userId, it.isCompleted) },
                        taskSolution.userId?.let { UserVO.fromData(it, null) },
                        taskSolution.language,
                        taskSolution.sourceCode,
                        taskSolution.attempt,
                        taskSolution.status
                )
    }
}