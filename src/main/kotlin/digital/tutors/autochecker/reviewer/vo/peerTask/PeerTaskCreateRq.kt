package digital.tutors.autochecker.reviewer.vo.peerTask

import digital.tutors.autochecker.checker.entities.Level
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.core.entity.EntityRefRq


data class PeerTaskCreateRq(
        var topicId: EntityRefRq? = null,
        var authorId: EntityRefRq? = null,
        var contributors: List<EntityRefRq>? = null,
        var level: Level?,
        var description: String?,
        var title: String?,
        var options: Options?,
        var criterions: List<String>?,
        var maxGradesPerCriterions: List<Int>?,
        var isCompleted: Boolean = false
)