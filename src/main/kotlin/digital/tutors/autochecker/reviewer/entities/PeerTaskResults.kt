package digital.tutors.autochecker.reviewer.entities

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.core.entity.AuditableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class PeerTaskResults(
        @Id var id: String? = null,

        @DBRef
        var taskId: PeerTask? = null,

        @DBRef
        var studentId: User? = null,

        var receivedReviews: Int = 0,
        var postedReviews: Int = 0,
        var grade: Int = 0,
        var completed: Boolean = false,
        var status: PeerTaskResultsStatus = PeerTaskResultsStatus.NOT_CHECKING
) : AuditableEntity()

enum class PeerTaskResultsStatus {
    NOT_CHECKING, IN_PROCESS, COMPLETED
}