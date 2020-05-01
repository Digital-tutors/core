package digital.tutors.autochecker.checker.vo.taskResults

data class TaskResultsMQ(
        val id: String,
        val taskId: String? = null,
        val userId: String? = null,
        val completed: Boolean = false,
        val codeReturn: String?,
        val messageOut: String?,
        val runtime: String?,
        val memory: String?
)
