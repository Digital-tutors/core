package digital.tutors.autochecker.checker.vo.task

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Level
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.Test
import digital.tutors.autochecker.checker.vo.topic.TopicVO

data class TaskVO(
        val id: String?,
        val topicId: TopicVO?,
        val authorId: UserVO?,
        val description: String?,
        val contributors: List<UserVO>?,
        val options: Options?,
        val tests: Test?,
        val title: String?,
        val level: Level?,
        val isComplete: Boolean
) {

    companion object {
        fun fromData(task: Task, userId: String?, isComplete: Boolean): TaskVO =
                TaskVO(
                        task.id,
                        task.topicId?.let { TopicVO.fromData(it, userId) },
                        task.authorId?.let { UserVO.fromData(it, null) },
                        task.description,
                        task.contributors?.map { UserVO.fromData(it, null) },
                        task.options,
                        task.tests?.apply {
                            input = input?.slice(0..0)
                            output = output?.slice(0..0)
                        },
                        task.title,
                        task.level,
                        isComplete
                )
    }

}
