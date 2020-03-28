package digital.tutors.autochecker.checker.vo

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.core.entity.AuditableEntity

data class TopicCreateRq(
        var id: String?,
        var title: String?,
        var accessType: AccessType?,
        var followers: List<UserVO>? = null,
        var authorId: UserVO? = null,
        var contributors: List<UserVO>? = null
) : AuditableEntity()