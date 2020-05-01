package digital.tutors.autochecker.checker.vo.taskResults

import digital.tutors.autochecker.core.entity.EntityRefRq

data class TaskResultsCreateRq(
        var taskId: EntityRefRq? = null,
        var userId: EntityRefRq? = null,
        var language: String?,
        var sourceCode: String?
)
