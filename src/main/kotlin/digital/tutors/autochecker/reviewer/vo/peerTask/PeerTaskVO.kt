package digital.tutors.autochecker.reviewer.vo.peerTask

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Level
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.reviewer.entities.PeerTask
import digital.tutors.autochecker.checker.vo.topic.TopicVO

data class PeerTaskVO (
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
        val isComplete: Boolean
) {
    companion object {
        fun fromData(peerTask: PeerTask, userId: String?, isCompleted: Boolean): PeerTaskVO =
                PeerTaskVO(
                        peerTask.id,
                        peerTask.topicId?.let { TopicVO.fromData(it, userId) },
                        peerTask.authorId?.let { UserVO.fromData(it, null) },
                        peerTask.description,
                        peerTask.contributors?.map { UserVO.fromData(it, null) },
                        peerTask.options,
                        peerTask.title,
                        peerTask.level,
                        peerTask.criterions,
                        peerTask.maxGradesPerCriterions,
                        isCompleted
                )
    }
}