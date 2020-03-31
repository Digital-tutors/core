package digital.tutors.autochecker.checker.vo.task

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.vo.tests.TestsVO
import digital.tutors.autochecker.checker.vo.topic.TopicVO

data class TaskVO(
        var id: String?,
        var topicId: TopicVO?,
        var authorId: UserVO?,
        var description: String?,
        var contributors: List<UserVO>?,
        var options: OptionsVO?,
        var tests: TestsVO?,
        var level: String

) {
    companion object {
        fun fromData(task: Task): TaskVO =
                TaskVO(
                        task.id,
                        task.topicId?.let { TopicVO.fromData(it) },
                        task.authorId?.let { UserVO.fromData(it, null) },
                        task.description,
                        task.contributors?.map { UserVO.fromData(it, null) },
                        task.options?.let { OptionsVO.fromData(it) },
                        task.tests?.let { TestsVO.fromData(it) },
                        task.level.toString()
                )
    }

}