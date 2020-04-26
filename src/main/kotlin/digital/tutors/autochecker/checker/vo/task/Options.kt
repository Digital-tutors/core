package digital.tutors.autochecker.checker.vo.task

import digital.tutors.autochecker.checker.entities.Options

data class Options (
    var constructions: List<String>?,
    var timeLimit: String?,
    var memoryLimit: String?
    ) {
    companion object {
        fun fromData(options: Options): digital.tutors.autochecker.checker.vo.task.Options =
                digital.tutors.autochecker.checker.vo.task.Options(
                        options.constructions,
                        options.timeLimit,
                        options.memoryLimit
                )
    }
}
