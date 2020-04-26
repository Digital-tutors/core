package digital.tutors.autochecker.checker.vo.task

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.vo.tests.TestsVO
import digital.tutors.autochecker.checker.vo.topic.TopicVO

data class TaskVO(
        val id: String?,
        val topicId: TopicVO?,
        val authorId: UserVO?,
        val description: String?,
        val contributors: List<UserVO>?,
        val options: Options?,
        val tests: TestsVO?,
        val level: String?

) {
    companion object {
        fun fromData(task: Task): TaskVO =
                TaskVO(
                        task.id,
                        task.topicId?.let { TopicVO.fromData(it) },
                        task.authorId?.let { UserVO.fromData(it, null) },
                        task.description,
                        task.contributors?.map { UserVO.fromData(it, null) },
                        task.options?.let { Options.fromData(it) },
                        task.tests?.let { TestsVO.fromData(it) },
                        task.level.toString()
                )
    }

}