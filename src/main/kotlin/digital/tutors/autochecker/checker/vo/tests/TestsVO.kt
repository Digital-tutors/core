package digital.tutors.autochecker.checker.vo.tests

import digital.tutors.autochecker.checker.entities.Tests

data class TestsVO (
        var input: List<String>?,
        var output: List<String>?
) {
    companion object {
        fun fromData(tests: Tests): TestsVO =
                TestsVO(
                        tests.input,
                        tests.output
                )
    }
}