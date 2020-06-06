package digital.tutors.autochecker.checker.vo.peerTask

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.checker.entities.Level
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.core.entity.EntityRefRq


data class PeerTaskCreateRq(
        var topicId: EntityRefRq? = null,
        var authorId: EntityRefRq? = null,
        var contributors: List<EntityRefRq>? = null,
        var level: Level?,
        var description: String?,
        var title: String?,
        var options: Options? = null,
        var criterions: List<String>? = null,
        var maxGradesPerCriterions: List<Int>? = null
)