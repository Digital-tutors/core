package digital.tutors.autochecker.checker.vo.topic

import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.core.entity.EntityRefRq

data class TopicCreateRq(
        var title: String?,
        var accessType: AccessType?,
        var followers: List<EntityRefRq>? = null,
        var authorId: EntityRefRq? = null,
        var contributors: List<EntityRefRq>? = null
)