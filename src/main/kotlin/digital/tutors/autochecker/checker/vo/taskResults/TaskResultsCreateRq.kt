package digital.tutors.autochecker.checker.vo.taskResults

import digital.tutors.autochecker.core.entity.EntityRefRq

data class TaskResultsCreateRq(
        var taskId: EntityRefRq? = null,
        var userId: EntityRefRq? = null,
        var language: String?,
        var sourceCode: String?,
        var completed: Boolean = false,
        var attempt: Int = 0,
        var codeReturn: String?,
        var messageOut: String?,
        var runtime: String?,
        var memory: String?
)
