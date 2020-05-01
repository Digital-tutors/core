package digital.tutors.autochecker.checker.vo.task

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.Test
import digital.tutors.autochecker.checker.vo.topic.TopicVO

data class TaskAdminVO(
        val id: String?,
        val topicId: TopicVO?,
        val authorId: UserVO?,
        val description: String?,
        val contributors: List<UserVO>?,
        val options: Options?,
        val test: Test?,
        val level: String?
) {

    companion object {
        fun fromData(task: Task): TaskAdminVO =
                TaskAdminVO(
                        task.id,
                        task.topicId?.let { TopicVO.fromData(it) },
                        task.authorId?.let { UserVO.fromData(it, null) },
                        task.description,
                        task.contributors?.map { UserVO.fromData(it, null) },
                        task.options,
                        task.test,
                        task.level.toString()
                )
    }

}
