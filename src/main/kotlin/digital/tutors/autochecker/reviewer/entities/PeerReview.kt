package digital.tutors.autochecker.reviewer.entities

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.core.entity.AuditableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class PeerReview(
        @Id
        var id: String? = null,

        @DBRef
        var taskId: PeerTask? = null,

        @DBRef
        var studentId: User? = null,

        @DBRef
        var expertId: User? = null,

        @DBRef
        var solutionId: PeerTaskSolution? = null,

        var grade: Double = 0.0,
        var gradesPerCriterions: List<Int>? = null,
        var argumentsPerCriterions: List<String>? = null,
        var summaryMessagePerSolution: String? = null
) : AuditableEntity()