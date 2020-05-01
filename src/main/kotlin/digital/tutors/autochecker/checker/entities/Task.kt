package digital.tutors.autochecker.checker.entities

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.core.entity.AuditableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Task(
        @Id var id: String? = null,

        @DBRef
        var topicId: Topic? = null,

        @DBRef
        var authorId: User? = null,

        @DBRef
        var contributors: List<User>? = null,

        var level: Level? = Level.JUNIOR,
        var description: String? = null,
        var options: Options? = null,
        var test: Test? = null
) : AuditableEntity()

enum class Level {
    JUNIOR, MIDDLE, SENIOR
}

data class Options(
        var constructions: List<String>? = null,
        var timeLimit: String? = null,
        var memoryLimit: String? = null
)

data class Test(
        var input: List<String>? = null,
        var output: List<String>? = null
)
