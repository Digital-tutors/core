package digital.tutors.autochecker.reviewer.vo.peerTask

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Level
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.reviewer.entities.PeerTask
import digital.tutors.autochecker.checker.vo.topic.TopicVO

data class PeerTaskAdminVO (
        val id: String?,
        val topicId: TopicVO?,
        val authorId: UserVO?,
        val description: String?,
        val contributors: List<UserVO>?,
        val options: Options?,
        val title: String?,
        val level: Level?,
        var criterions: List<String>? = null,
        var maxGradesPerCriterions: List<Int>? = null,
        var isCompleted: Boolean = false
) {
    companion object {
        fun fromData(task: PeerTask): PeerTaskAdminVO =
                PeerTaskAdminVO(
                        task.id,
                        task.topicId?.let { TopicVO.fromData(it, null) },
                        task.authorId?.let { UserVO.fromData(it, null) },
                        task.description,
                        task.contributors?.map { UserVO.fromData(it, null) },
                        task.options,
                        task.title,
                        task.level,
                        task.criterions,
                        task.maxGradesPerCriterions,
                        task.isCompleted
                )
    }
}