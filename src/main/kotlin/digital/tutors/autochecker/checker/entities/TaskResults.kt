package digital.tutors.autochecker.checker.entities

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.core.entity.AuditableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class TaskResults(
        @Id var id: String? = null,

        @DBRef
        var taskId: Task? = null,

        @DBRef
        var userId: User? = null,

        var language: String? = null,
        var sourceCode: String? = null,

        var completed: Boolean = false,
        var attempt: Int = 0,
        var codeReturn: String? = null,
        var messageOut: String? = null,
        var runtime: String? = null,
        var memory: String? = null
) : AuditableEntity()
