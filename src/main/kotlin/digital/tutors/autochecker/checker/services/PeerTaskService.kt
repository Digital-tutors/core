package digital.tutors.autochecker.checker.services

import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskAdminVO
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskCreateRq
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskUpdateRq
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskVO
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PeerTaskService {

    @Throws(EntityNotFoundException::class)
    fun getPeerTasksByAuthorId(authorId: String): List<PeerTaskVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerTasksByTopicId(topicId: String): List<PeerTaskVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskByIdOrThrow(id: String): PeerTaskVO

    @Throws(EntityNotFoundException::class)
    fun getAdminPeerTaskByIdOrThrow(id: String): PeerTaskAdminVO

    @Throws(EntityNotFoundException::class)
    fun getPeerTasks(pageable: Pageable): Page<PeerTaskAdminVO>

    @Throws(EntityNotFoundException::class)
    fun createPeerTask(peerTaskCreateRq: PeerTaskCreateRq): PeerTaskVO

    @Throws(EntityNotFoundException::class)
    fun updatePeerTask(id: String, peerTaskUpdateRq: PeerTaskUpdateRq): PeerTaskVO

    @Throws(EntityNotFoundException::class)
    fun delete(id: String)
}