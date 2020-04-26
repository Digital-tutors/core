package digital.tutors.autochecker.checker.vo.tests

import digital.tutors.autochecker.checker.entities.Tests

data class TestsVO (
        val input: List<String>?,
        val output: List<String>?
) {
    companion object {
        fun fromData(tests: Tests): TestsVO =
                TestsVO(
                        tests.input,
                        tests.output
                )
    }
}