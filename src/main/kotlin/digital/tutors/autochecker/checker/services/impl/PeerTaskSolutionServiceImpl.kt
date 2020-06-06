package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.checker.entities.PeerTask
import digital.tutors.autochecker.checker.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.checker.entities.PeerTaskSolution
import digital.tutors.autochecker.checker.repositories.PeerTaskRepository
import digital.tutors.autochecker.checker.repositories.PeerTaskSolutionRepository
import digital.tutors.autochecker.checker.services.PeerTaskSolutionService
import digital.tutors.autochecker.checker.vo.peerTaskSolution.PeerTaskSolutionCreateRq
import digital.tutors.autochecker.checker.vo.peerTaskSolution.PeerTaskSolutionVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.entity.EntityRefRq
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PeerTaskSolutionServiceImpl: PeerTaskSolutionService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var peerTaskSolutionRepository: PeerTaskSolutionRepository

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskSolutionsByUserAndTask(userId: String, taskId: String): List<PeerTaskSolutionVO> {
        return peerTaskSolutionRepository.findAllByUserIdAndTaskId(User(id = userId), PeerTask(id = taskId)).map(::toPeerTaskSolutionVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskSolutionsByUser(userId: String): List<PeerTaskSolutionVO> {
        return peerTaskSolutionRepository.findAllByUserId(User(id = userId)).map(::toPeerTaskSolutionVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskSolutionsByTaskId(taskId: String): List<PeerTaskSolutionVO> {
        return peerTaskSolutionRepository.findAllByTaskId(PeerTask(id = taskId)).map(::toPeerTaskSolutionVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskSolutionByIdOrThrow(id: String): PeerTaskSolutionVO {
        return peerTaskSolutionRepository.findById(id).map(::toPeerTaskSolutionVO).orElseThrow { throw EntityNotFoundException("Solution with $id not found") }
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskSolutions(pageable: Pageable): Page<PeerTaskSolutionVO> {
        return peerTaskSolutionRepository.findAll(pageable).map(::toPeerTaskSolutionVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun savePeerTaskSolution(peerTaskSolutionCreateRq: PeerTaskSolutionCreateRq): PeerTaskSolutionVO {
        val id = peerTaskSolutionRepository.save(PeerTaskSolution().apply {
            taskId = PeerTask(id = peerTaskSolutionCreateRq.taskId?.id)
            userId = User(id = peerTaskSolutionCreateRq.userId?.id)
            language = peerTaskSolutionCreateRq.language
            sourceCode = peerTaskSolutionCreateRq.sourceCode
            attempt = peerTaskSolutionCreateRq.attempt
            status = peerTaskSolutionCreateRq.status
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Created entity $id")
        return getPeerTaskSolutionByIdOrThrow(id)
    }

    override fun delete(id: String) {
        peerTaskSolutionRepository.deleteById(id)
        log.debug("Deleted solution entity $id")
    }

    private fun toPeerTaskSolutionVO(peerTaskSolution: PeerTaskSolution): PeerTaskSolutionVO {
        return PeerTaskSolutionVO.fromData(peerTaskSolution, authorizationService.currentUserIdOrDie())
    }
}