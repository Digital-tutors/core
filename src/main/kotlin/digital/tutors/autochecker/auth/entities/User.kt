package digital.tutors.autochecker.auth.entities

import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.core.entity.AuditableEntity
import org.hibernate.validator.constraints.UniqueElements
import org.springframework.context.annotation.Primary
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.security.access.annotation.Secured
import java.lang.annotation.ElementType
import javax.validation.constraints.Email
import javax.validation.constraints.Size

// TODO: Добавить отчество

@Document
data class User(
        @Id var id: String? = null,


        @get:Email
        var email: String? = null,

        @get:Size(min = 8, max = 180)
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
