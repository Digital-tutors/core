package digital.tutors.autochecker.checker.vo.peerTask

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.checker.entities.Level
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.checker.entities.Topic


data class PeerTaskCreateRq(
        var topicId: Topic? = null,
        var authorId: User? = null,
        var contributors: List<User>? = null,
        var level: Level?,
        var description: String?,
        var title: String?,
        var options: Options? = null,
        var criterions: List<String>? = null,
        var maxGradesPerCriterions: List<Int>? = null
)