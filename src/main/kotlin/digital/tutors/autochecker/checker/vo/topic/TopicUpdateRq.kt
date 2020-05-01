package digital.tutors.autochecker.checker.vo.topic

import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.core.entity.EntityRefRq

data class TopicUpdateRq(
        val title: String?,
        val accessType: AccessType?,
        val followers: List<EntityRefRq>? = null,
        val contributors: List<EntityRefRq>? = null
)