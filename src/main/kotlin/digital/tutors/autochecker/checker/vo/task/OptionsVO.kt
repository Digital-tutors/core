package digital.tutors.autochecker.checker.vo.task

import digital.tutors.autochecker.checker.entities.Options

data class OptionsVO (
    var constructions: List<String>?,
    var timeLimit: String?,
    var memoryLimit: String?
    ) {
    companion object {
        fun fromData(options: Options): OptionsVO =
                OptionsVO(
                        options.constructions,
                        options.timeLimit,
                        options.memoryLimit
                )
    }
}
