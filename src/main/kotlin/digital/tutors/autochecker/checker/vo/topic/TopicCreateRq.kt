package digital.tutors.autochecker.checker.vo.topic

import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.core.entity.EntityRefRq

data class TopicCreateRq(
        val title: String?,
        val accessType: AccessType?,
        val followers: List<EntityRefRq>? = null,
        val authorId: EntityRefRq? = null,
        val contributors: List<EntityRefRq>? = null
)