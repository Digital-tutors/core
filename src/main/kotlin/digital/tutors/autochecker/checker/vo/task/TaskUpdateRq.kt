package digital.tutors.autochecker.checker.vo.task

import digital.tutors.autochecker.checker.vo.tests.TestsVO

data class TaskUpdateRq (
        var description: String?,
        var options: Options?,
        var tests: TestsVO?
)