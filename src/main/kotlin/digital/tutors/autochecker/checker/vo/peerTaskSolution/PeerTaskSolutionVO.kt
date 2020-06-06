package digital.tutors.autochecker.checker.vo.peerTaskSolution

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.PeerTask
import digital.tutors.autochecker.checker.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.checker.entities.PeerTaskSolution
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskVO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import java.util.*

data class PeerTaskSolutionVO(
        var id: String?,
        var taskId: PeerTaskVO?,
        var userId: UserVO?,
        var language: String?,
        var sourceCode: String?,
        var attempt: Int,
        var status: PeerTaskResultsStatus,
        var createdAt: Date
){
    companion object {
        fun fromData(taskSolution: PeerTaskSolution, userId: String?) : PeerTaskSolutionVO =
                PeerTaskSolutionVO(
                        taskSolution.id,
                        taskSolution.taskId?.let { PeerTaskVO.fromData (it, userId, it.isCompleted) },
                        taskSolution.userId?.let { UserVO.fromData(it, null) },
                        taskSolution.language,
                        taskSolution.sourceCode,
                        taskSolution.attempt,
                        taskSolution.status,
                        taskSolution.createdAt
                )
    }
}