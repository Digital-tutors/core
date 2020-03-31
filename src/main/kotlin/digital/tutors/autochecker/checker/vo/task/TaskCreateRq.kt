package digital.tutors.autochecker.checker.vo.task

import digital.tutors.autochecker.checker.vo.tests.TestsVO
import digital.tutors.autochecker.core.entity.EntityRefRq

data class TaskCreateRq (
        var authorId: EntityRefRq?,
        var descriptions: String?,
        var options: OptionsVO?,
        var test: TestsVO?
)