package digital.tutors.autochecker.checker.vo.taskResults

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.TaskResults
import digital.tutors.autochecker.checker.vo.task.TaskVO

data class TaskResultsVO(
        val id: String?,
        val taskId: TaskVO? = null,
        val userId: UserVO? = null,
        val language: String?,
        val completed: Boolean = false,
        val attempt: Int = 0,
        val codeReturn: String?,
        val messageOut: String?,
        val runtime: String?,
        val memory: String?,
        val status: String?
) {

    companion object {
        fun fromData(taskResults: TaskResults): TaskResultsVO =
                TaskResultsVO(
                        taskResults.id,
                        taskResults.taskId?.let { TaskVO.fromData(it) },
                        taskResults.userId?.let { UserVO.fromData(it, null) },
                        taskResults.language,
                        taskResults.completed,
                        taskResults.attempt,
                        taskResults.codeReturn,
                        taskResults.messageOut,
                        taskResults.runtime,
                        taskResults.memory,
                        taskResults.status.toString()
                )
    }

}
