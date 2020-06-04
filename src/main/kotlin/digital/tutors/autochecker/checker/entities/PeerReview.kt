package digital.tutors.autochecker.checker.entities

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.core.entity.AuditableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class PeerReview (
        @Id var id: String? = null,

        @DBRef var taskId: PeerTask? = null,

        @DBRef var studentId: User? = null,

        @DBRef var expertId: User? = null,

        @DBRef var solutionId: PeerTaskSolution? = null,

        @DBRef
        var gradesPerCriterions: List<Int>? = null,
        var argumentsPerCriterions: List<String>? = null,
        var summaryMessagePerSolution: String? = null,
        var createdAt: Date = Date(0),
        var updatedAt: Date = Date(0)
): AuditableEntity()