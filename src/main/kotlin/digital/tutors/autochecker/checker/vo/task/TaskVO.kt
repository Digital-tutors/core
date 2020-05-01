package digital.tutors.autochecker.checker.vo.task

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Level
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.Test
import digital.tutors.autochecker.checker.vo.topic.TopicVO

data class TaskVO(
        var id: String?,
        var topicId: TopicVO?,
        var authorId: UserVO?,
        var description: String?,
        var contributors: List<UserVO>?,
        var options: Options?,
        var test: Test?,
        var level: Level?
) {

    companion object {
        fun fromData(task: Task): TaskVO =
                TaskVO(
                        task.id,
                        task.topicId?.let { TopicVO.fromData(it) },
                        task.authorId?.let { UserVO.fromData(it, null) },
                        task.description,
                        task.contributors?.map { UserVO.fromData(it, null) },
                        task.options,
                        task.test,
                        task.level
                )
    }

}
