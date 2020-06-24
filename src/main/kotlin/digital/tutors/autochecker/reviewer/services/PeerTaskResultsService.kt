package digital.tutors.autochecker.reviewer.services

import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsCreateRq
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsUpdateRq
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsVO
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import digital.tutors.autochecker.reviewer.entities.PeerTaskResults
import org.springframework.amqp.AmqpException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PeerTaskResultsService {

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskResultsByUserAndTask(userId: String, taskId: String): List<PeerTaskResultsVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskResultsByUser(userId: String): List<PeerTaskResultsVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskResultsByTaskId(taskId: String): List<PeerTaskResultsVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskResultByIdOrThrow(id: String): PeerTaskResultsVO

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskResultByPeerTaskIdAndUser(peerTaskId: String, userId: String): PeerTaskResultsVO

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskResults(pageable: Pageable): Page<PeerTaskResultsVO>

    @Throws(EntityNotFoundException::class, AmqpException::class)
    fun createPeerTaskResult(peerTaskResultsCreateRq: PeerTaskResultsCreateRq): PeerTaskResultsVO

    @Throws(EntityNotFoundException::class, AmqpException::class)
    fun updatePeerTaskResult(id: String, peerTaskResultsUpdateRq: PeerTaskResultsUpdateRq): PeerTaskResultsVO

}