package digital.tutors.autochecker.checker.entities

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.core.entity.AuditableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Topic(
        @Id var id: String? = null,
        var title: String? = null,
        var accessType: AccessType? = AccessType.PRIVATE,

        @DBRef
        var followers: List<User>? = null,

        @DBRef
        var authorId: User? = null,

        @DBRef
        var contributors: List<User>? = null
) : AuditableEntity()

enum class AccessType {
    PUBLIC, PRIVATE
}
