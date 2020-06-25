package digital.tutors.autochecker.reviewer.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.repositories.UserRepository
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.reviewer.entities.PeerReview
import digital.tutors.autochecker.reviewer.entities.PeerTask
import digital.tutors.autochecker.reviewer.entities.PeerTaskResults
import digital.tutors.autochecker.reviewer.repositories.PeerTaskResultsRepository
import digital.tutors.autochecker.reviewer.services.PeerTaskResultsService
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsCreateRq
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsUpdateRq
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.entity.EntityRefRq
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.reviewer.repositories.PeerTaskRepository
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewVO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PeerTaskResultsServiceImpl: PeerTaskResultsService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var peerTaskResultsRepository: PeerTaskResultsRepository

    @Autowired
    lateinit var peerTaskRepository: PeerTaskRepository

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResultsByUserAndTask(userId: String, taskId: String): List<PeerTaskResultsVO> {
        val user = userRepository.findByIdOrNull(userId)
                ?: throw EntityNotFoundException("User with $userId not found.")
        return peerTaskResultsRepository.findAllByStudentIdAndTaskId(user, PeerTask(id = taskId)).map(::toPeerTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResultsByUser(userId: String): List<PeerTaskResultsVO> {
        val user = userRepository.findByIdOrNull(userId)
                ?: throw EntityNotFoundException("User with $userId not found.")
        return peerTaskResultsRepository.findAllByStudentId(user).map(::toPeerTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResultsByTaskId(taskId: String): List<PeerTaskResultsVO> {
        val task = peerTaskRepository.findByIdOrNull(taskId) ?: throw EntityNotFoundException("User with $taskId not found.")
        return  peerTaskResultsRepository.findAllByTaskId(task).map(::toPeerTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResultByIdOrThrow(id: String): PeerTaskResultsVO {
        return peerTaskResultsRepository.findById(id).map(::toPeerTaskResultsVO).orElseThrow { throw EntityNotFoundException("Task results with $id not found") }
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResults(pageable: Pageable): Page<PeerTaskResultsVO> {
        return peerTaskResultsRepository.findAll(pageable).map(::toPeerTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResultByPeerTaskIdAndUser(peerTaskId: String, userId: String): PeerTaskResultsVO {
        val user = userRepository.findByIdOrNull(userId)
                ?: throw EntityNotFoundException("User with $userId not found.")
        val task = peerTaskRepository.findByIdOrNull(peerTaskId) ?: throw EntityNotFoundException("Task with $peerTaskId not found.")

        val result = peerTaskResultsRepository.findFirstByStudentIdAndTaskId(user, task) ?: throw EntityNotFoundException("Result not found.")
        return toPeerTaskResultsVO(result)
    }

    override fun createPeerTaskResult(peerTaskResultsCreateRq: PeerTaskResultsCreateRq): PeerTaskResultsVO {
        val id = peerTaskResultsRepository.save(PeerTaskResults().apply {
            taskId = PeerTask(id = peerTaskResultsCreateRq.taskId?.id)
            studentId = User(id = peerTaskResultsCreateRq.studentId?.id)
            receivedReviews = 0
            postedReviews = 0
            grade = 0
            completed = false
            status = PeerTaskResultsStatus.NOT_CHECKING
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Created entity $id")
        return getPeerTaskResultByIdOrThrow(id)
    }

    override fun updatePeerTaskResult(id: String, peerTaskResultsUpdateRq: PeerTaskResultsUpdateRq): PeerTaskResultsVO {
        peerTaskResultsRepository.save(peerTaskResultsRepository.findById(id).get().apply {
            completed = peerTaskResultsUpdateRq.completed
            status = peerTaskResultsUpdateRq.status
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Created entity $id")
        return getPeerTaskResultByIdOrThrow(id)
    }

    private fun toPeerTaskResultsVO(peerTaskResults: PeerTaskResults): PeerTaskResultsVO {
        return PeerTaskResultsVO.fromData(peerTaskResults, authorizationService.currentUserIdOrDie(), peerTaskResults.completed)
    }
}