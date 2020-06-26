package digital.tutors.autochecker.reviewer.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.repositories.UserRepository
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.reviewer.entities.PeerTask
import digital.tutors.autochecker.reviewer.entities.PeerTaskSolution
import digital.tutors.autochecker.reviewer.repositories.PeerTaskSolutionRepository
import digital.tutors.autochecker.reviewer.services.PeerTaskSolutionService
import digital.tutors.autochecker.reviewer.vo.peerTaskSolution.PeerTaskSolutionCreateRq
import digital.tutors.autochecker.reviewer.vo.peerTaskSolution.PeerTaskSolutionVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import digital.tutors.autochecker.reviewer.entities.PeerTaskResults
import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.reviewer.repositories.PeerReviewRepository
import digital.tutors.autochecker.reviewer.repositories.PeerTaskRepository
import digital.tutors.autochecker.reviewer.repositories.PeerTaskResultsRepository
import digital.tutors.autochecker.reviewer.repositories.impl.PeerTaskResultsRepositoryImpl
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsVO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PeerTaskSolutionServiceImpl : PeerTaskSolutionService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var peerTaskSolutionRepository: PeerTaskSolutionRepository

    @Autowired
    lateinit var peerReviewRepository: PeerReviewRepository

    @Autowired
    lateinit var peerTaskResultsRepository: PeerTaskResultsRepository

    @Autowired
    lateinit var peerTaskResultsRepositoryImpl: PeerTaskResultsRepositoryImpl

    @Autowired
    lateinit var peerTaskRepository: PeerTaskRepository

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskSolutionsByUserAndTask(userId: String, taskId: String): List<PeerTaskSolutionVO> {
        val user = userRepository.findByIdOrNull(userId)
                ?: throw EntityNotFoundException("User with $userId not found.")
        val task = peerTaskRepository.findByIdOrNull(taskId)
                ?: throw EntityNotFoundException("Task with $taskId not found.")

        return peerTaskSolutionRepository.findAllByUserIdAndTaskId(user, task).map(::toPeerTaskSolutionVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskSolutionsByUser(userId: String): List<PeerTaskSolutionVO> {
        val user = userRepository.findByIdOrNull(userId)
                ?: throw EntityNotFoundException("User with $userId not found.")
        return peerTaskSolutionRepository.findAllByUserId(user).map(::toPeerTaskSolutionVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskSolutionsByTaskId(taskId: String): List<PeerTaskSolutionVO> {
        val task = peerTaskRepository.findByIdOrNull(taskId)
                ?: throw EntityNotFoundException("Task with $taskId not found.")

        return peerTaskSolutionRepository.findAllByTaskId(task).map(::toPeerTaskSolutionVO)
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
    private fun getPeerTaskSolutionByUserAndTask(user: User, peerTask: PeerTask): PeerTaskSolutionVO {
        val userSolution = peerTaskSolutionRepository.findFirstByUserIdAndTaskId(user, peerTask)
                ?: throw EntityNotFoundException("Solution not found")
        return toPeerTaskSolutionVO(userSolution)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskSolutionOfRandomUserByPeerTask(id: String): PeerTaskSolutionVO {

        val currentUser = userRepository.findByIdOrNull(authorizationService.currentUserIdOrDie())
                ?: throw EntityNotFoundException("User not found")

        val peerTask = peerTaskRepository.findById(id).orElseThrow { throw EntityNotFoundException("Task with $id not found") }

        val studentsList = peerReviewRepository.findDistinctByExpertIdAndTaskId(currentUser, peerTask).mapNotNull { it.studentId }

        val randomUserResult = peerTaskResultsRepositoryImpl.getRandomUserResult(PeerTaskResultsStatus.NOT_CHECKING, peerTask, currentUser, studentsList.plusElement(currentUser))
                ?: throw EntityNotFoundException("Results not found")

        val solution = getPeerTaskSolutionByUserAndTask(randomUserResult.studentId!!, peerTask)

        peerTaskResultsRepositoryImpl.setStatusForPeerTaskResults(PeerTaskResultsStatus.IN_PROCESS, randomUserResult.id!!)
        return solution
    }

    @Transactional
    override fun savePeerTaskSolution(peerTaskSolutionCreateRq: PeerTaskSolutionCreateRq): PeerTaskSolutionVO {

        val peerTask = peerTaskRepository.findById(peerTaskSolutionCreateRq.taskId?.id!!).orElseThrow { throw EntityNotFoundException("Task not found") }
        val user = userRepository.findByIdOrNull(peerTaskSolutionCreateRq.userId?.id!!)
                ?: throw EntityNotFoundException("User not found.")

        val id = peerTaskSolutionRepository.save(PeerTaskSolution().apply {
            taskId = peerTask
            userId = user
            language = peerTaskSolutionCreateRq.language
            sourceCode = peerTaskSolutionCreateRq.sourceCode
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        val resultsId = peerTaskResultsRepository.save(PeerTaskResults().apply {
            taskId = peerTask
            studentId = user
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