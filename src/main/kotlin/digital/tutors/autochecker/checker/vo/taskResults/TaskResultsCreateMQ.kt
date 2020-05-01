package digital.tutors.autochecker.checker.vo.taskResults

import digital.tutors.autochecker.core.entity.EntityRefRq

data class TaskResultsCreateMQ(
        var id: String? = null,
        var taskId: EntityRefRq? = null,
        var userId: EntityRefRq? = null,
        var language: String?,
        var sourceCode: String?
)
