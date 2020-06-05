package digital.tutors.autochecker.checker.services

import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskVO
import digital.tutors.autochecker.core.exception.EntityNotFoundException

interface PeerTaskService {

    @Throws(EntityNotFoundException::class)
    fun getTasksByAuthorId(authorId: String): List<PeerTaskVO>
}