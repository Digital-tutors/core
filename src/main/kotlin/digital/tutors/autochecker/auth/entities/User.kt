package digital.tutors.autochecker.auth.entities

import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.core.entity.AuditableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@Document
data class User(
        @Id var id: String? = null,

        @get:Email
        var email: String? = null,

        @get:Size(min = 6)
        var password: String? = null,

        @get:Size(min = 1, max = 20)
        var firstName: String? = null,

        @get:Size(min = 1, max = 20)
        var lastName: String? = null,

        var role: Roles = Roles.ROLE_USER,
        var confirmed: Boolean = false,

        @DBRef
        var topics: List<Topic>? = null
) : AuditableEntity()

enum class Roles {
    ROLE_USER, ROLE_STUDENT, ROLE_TEACHER, ROLE_ADMIN, ROLE_SUPER_ADMIN
}
