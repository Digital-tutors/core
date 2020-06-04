package digital.tutors.autochecker.checker.entities

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.core.entity.AuditableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class PeerTaskSolution(
        @Id var id: String? = null,

        @DBRef
        var taskId: PeerTask? = null,

        @DBRef
        var userId: User? = null,

        @DBRef
        var language: String? = null,
        var sourceCode: String? = null,
        var attempt: Int = 0,
        var status: PeerTaskResultsStatus = PeerTaskResultsStatus.NOT_CHECKING,
        var createdAt: Date = Date(0),
        var updatedAt: Date = Date(0)
): AuditableEntity()