package digital.tutors.autochecker.checker.services

import digital.tutors.autochecker.checker.vo.peerTaskSolution.PeerTaskSolutionCreateRq
import digital.tutors.autochecker.checker.vo.peerTaskSolution.PeerTaskSolutionVO
import digital.tutors.autochecker.checker.vo.taskResults.TaskResultsCreateRq
import digital.tutors.autochecker.checker.vo.taskResults.TaskResultsVO
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.amqp.AmqpException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PeerTaskSolutionService {

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskSolutionsByUserAndTask(userId: String, taskId: String): List<PeerTaskSolutionVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskSolutionsByUser(userId: String): List<PeerTaskSolutionVO>

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskSolutionsByTaskId(taskId: String): List<PeerTaskSolutionVO>

    @Throws(EntityNotFoundException::class)
    fun getFirstPeerTaskSolutionByUserAndTask(userId: String, taskId: String): PeerTaskSolutionVO

    @Throws(EntityNotFoundException::class)
    fun getFirstPeerTaskSolutionByUser(userId: String): PeerTaskSolutionVO

    @Throws(EntityNotFoundException::class)
    fun getFirstPeerTaskSolutionByTaskId(taskId: String): PeerTaskSolutionVO

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskSolutionByIdOrThrow(id: String): PeerTaskSolutionVO

    @Throws(EntityNotFoundException::class)
    fun getPeerTaskSolutions(pageable: Pageable): Page<PeerTaskSolutionVO>

    @Throws(EntityNotFoundException::class, AmqpException::class)
    fun savePeerTaskSolution(peerTaskSolutionCreateRq: PeerTaskSolutionCreateRq): PeerTaskSolutionVO

    @Throws(EntityNotFoundException::class)
    fun delete(id: String)

}