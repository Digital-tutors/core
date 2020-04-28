package digital.tutors.autochecker.checker.vo.task

import digital.tutors.autochecker.checker.entities.Level
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.checker.entities.Tests
import digital.tutors.autochecker.core.entity.EntityRefRq

data class TaskCreateRq(
        var authorId: EntityRefRq? = null,
        var topicId: EntityRefRq? = null,
        var contributors: List<EntityRefRq>? = null,
        var level: Level?,
        var description: String?,
        var options: Options?,
        var tests: Tests?
)
