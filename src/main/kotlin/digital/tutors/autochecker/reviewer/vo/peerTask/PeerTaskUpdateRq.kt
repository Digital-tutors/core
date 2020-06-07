package digital.tutors.autochecker.reviewer.vo.peerTask

import digital.tutors.autochecker.checker.entities.Level
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.core.entity.EntityRefRq

data class PeerTaskUpdateRq(
        var topicId: EntityRefRq? = null,
        var contributors: List<EntityRefRq>? = null,
        var level: Level?,
        var description: String?,
        var options: Options?,
        var title: String?,
        var criterions: List<String>? = null,
        var maxGradesPerCriterions: List<Int>? = null,
        var isCompleted: Boolean = false
)