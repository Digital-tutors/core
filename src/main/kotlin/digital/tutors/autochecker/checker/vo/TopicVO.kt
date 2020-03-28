package digital.tutors.autochecker.checker.vo

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.checker.entities.Topic

data class TopicVO(
        var id: String?,
        var title: String?,
        var accessType: AccessType?,
        var followers: List<UserVO>?,
        var authorId: UserVO?,
        var contributors: List<UserVO>?,
        var createdDate: String?
) {

    companion object {
        fun fromData(topic: Topic): TopicVO =
                TopicVO(
                        topic.id,
                        topic.title,
                        topic.accessType,
                        topic.followers?.map { UserVO.fromData(it, null) },
                        UserVO.fromData(topic.authorId!!, null),
                        topic.contributors?.map { UserVO.fromData(it, null) },
                        topic.createdBy
                )
    }

}