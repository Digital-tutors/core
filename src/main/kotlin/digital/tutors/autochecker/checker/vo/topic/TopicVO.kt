package digital.tutors.autochecker.checker.vo.topic

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.checker.entities.Topic

data class TopicVO(
        val id: String?,
        val title: String?,
        val accessType: AccessType?,
        val followers: List<UserVO>?,
        val authorId: UserVO?,
        val contributors: List<UserVO>?,
        val createdDate: String?,
        val isSubscribe: Boolean
) {

    companion object {
        fun fromData(topic: Topic, userId: String?): TopicVO =
                TopicVO(
                        topic.id,
                        topic.title,
                        topic.accessType,
                        topic.followers?.map { UserVO.fromData(it, null) },
                        topic.authorId?.let { UserVO.fromData(it, null) },
                        topic.contributors?.map { UserVO.fromData(it, null) },
                        topic.createdBy,
                        topic.followers?.takeWhile { it.id == userId }?.isNotEmpty() ?: false
                )
    }

}
