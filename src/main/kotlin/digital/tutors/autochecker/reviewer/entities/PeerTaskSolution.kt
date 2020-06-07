package digital.tutors.autochecker.reviewer.entities

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.core.entity.AuditableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class PeerTaskSolution(
        @Id var id: String? = null,

        @DBRef
        var taskId: PeerTask? = null,

        @DBRef
        var userId: User? = null,

        var language: String? = null,
        var sourceCode: String? = null,
        var attempt: Int = 0,
        var status: PeerTaskResultsStatus = PeerTaskResultsStatus.NOT_CHECKING
) : AuditableEntity()