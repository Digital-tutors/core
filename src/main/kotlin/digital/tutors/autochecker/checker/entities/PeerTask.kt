package digital.tutors.autochecker.checker.entities

import digital.tutors.autochecker.core.entity.AuditableEntity
import digital.tutors.autochecker.auth.entities.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class PeerTask (
        @Id var id: String? = null,

        @DBRef var topicId: Topic? = null,

        @DBRef var authorId: User? = null,

        @DBRef
        var contributors: List<User>? = null,
        var level: Level? = Level.JUNIOR,
        var description: String? = null,
        var title: String? = null,
        var options: Options? = null,
        var criterions: List<String>? = null,
        var maxGradesPerCriterions: List<Int>? = null
): AuditableEntity()
