package digital.tutors.autochecker.reviewer.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.repositories.UserRepository
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.checker.entities.TaskResults
import digital.tutors.autochecker.checker.repositories.TopicRepository
import digital.tutors.autochecker.reviewer.entities.PeerReview
import digital.tutors.autochecker.reviewer.entities.PeerTask
import digital.tutors.autochecker.reviewer.entities.PeerTaskSolution
import digital.tutors.autochecker.reviewer.repositories.PeerReviewRepository
import digital.tutors.autochecker.reviewer.services.PeerReviewService
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewCreateRq
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewUpdateRq
import digital.tutors.autochecker.reviewer.vo.peerReview.PeerReviewVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.reviewer.repositories.PeerTaskRepository
import digital.tutors.autochecker.reviewer.repositories.PeerTaskResultsRepository
import digital.tutors.autochecker.reviewer.repositories.PeerTaskSolutionRepository
import digital.tutors.autochecker.reviewer.repositories.impl.PeerTaskResultsRepositoryImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PeerReviewServiceImpl : PeerReviewService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var peerReviewRepository: PeerReviewRepository

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var peerTaskSolutionRepository: PeerTaskSolutionRepository

    @Autowired
    lateinit var peerTaskRepository: PeerTaskRepository

    @Autowired
    lateinit var peerTaskResultsRepository: PeerTaskResultsRepository

    @Autowired
    lateinit var peerTaskResultsRepositoryImpl: PeerTaskResultsRepositoryImpl

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsByExpertId(expertId: String): List<PeerReviewVO> {
        val expert = userRepository.findByIdOrNull(expertId)
                ?: throw EntityNotFoundException("User with $expertId not found.")
        return peerReviewRepository.findAllByExpertId(expert).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsByStudentId(studentId: String): List<PeerReviewVO> {
        val student = userRepository.findByIdOrNull(studentId)
                ?: throw EntityNotFoundException("User with $studentId not found.")
        return peerReviewRepository.findAllByStudentIdOrderByCreatedDtAsc(student).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsByPeerTaskId(taskId: String): List<PeerReviewVO> {
        val task = peerTaskRepository.findByIdOrNull(taskId)
                ?: throw EntityNotFoundException("Task with $taskId not found.")

        return peerReviewRepository.findAllByTaskId(task).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsBySolutionId(solutionId: String): List<PeerReviewVO> {
        val solution = peerTaskSolutionRepository.findByIdOrNull(solutionId)
                ?: throw EntityNotFoundException("Solution with $solutionId not found.")
        return peerReviewRepository.findAllBySolutionId(solution).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsByStudentIdAndTaskId(studentId: String, peerTaskId: String): List<PeerReviewVO> {
        val student =  userRepository.findByIdOrNull(studentId) ?: throw EntityNotFoundException("User with $studentId not found")
        val peerTask = peerTaskRepository.findByIdOrNull(peerTaskId) ?: throw EntityNotFoundException("Task with $peerTaskId not found")

        val reviews = peerReviewRepository.findAllByStudentIdAndTaskIdOrderByCreatedDtAsc(student, peerTask)

        val receivedReviewsArray = mutableListOf<PeerReview>()
        receivedReviewsArray.groupBy { it.grade }.toList().forEach { (_, review) ->
            review.sortedByDescending { it.solutionId?.id }
            receivedReviewsArray.addAll(review)
        }
        return receivedReviewsArray.map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewByIdOrThrow(id: String): PeerReviewVO {
        return peerReviewRepository.findById(id).map(::toPeerReviewVO).orElseThrow { throw EntityNotFoundException("Peer review with $id not found") }
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviews(pageable: Pageable): Page<PeerReviewVO> {
        return peerReviewRepository.findAll(pageable).map(::toPeerReviewVO)
    }

    @Transactional
    @Throws(EntityNotFoundException::class)
    override fun createPeerReview(peerReviewCreateRq: PeerReviewCreateRq): PeerReviewVO {

        // TODO: возращение статуса в NOT_CHECKING в случае ошибки
        val peerTask = peerTaskRepository.findById(peerReviewCreateRq.taskId?.id!!).orElseThrow { throw EntityNotFoundException("Task not found") }
        val student = userRepository.findByIdOrNull(peerReviewCreateRq.studentId?.id!!)
                ?: throw EntityNotFoundException("User not found.")
        val expert = userRepository.findByIdOrNull(peerReviewCreateRq.expertId?.id!!)
                ?: throw EntityNotFoundException("User not found.")
        val peerTaskSolution = peerTaskSolutionRepository.findById(peerReviewCreateRq.solutionId?.id!!).orElseThrow { throw EntityNotFoundException("Solution not found") }

        val id = peerReviewRepository.save(PeerReview().apply {
            taskId = peerTask
            studentId = student
            expertId = expert
            solutionId = peerTaskSolution
            argumentsPerCriterions = peerReviewCreateRq.argumentsPerCriterions
            gradesPerCriterions = peerReviewCreateRq.gradesPerCriterions
            summaryMessagePerSolution = peerReviewCreateRq.summaryMessagePerSolution
            grade = peerReviewCreateRq.grade!!
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        peerTaskResultsRepositoryImpl.run {
            updateFields(expert, isPostedReviews = true, isReceivedReviews = false, peerTask = peerTask)
            updateFields(student, isPostedReviews = false, isReceivedReviews = true, peerTask = peerTask)
        }
        log.debug("Created entity $id")
        return getPeerReviewByIdOrThrow(id)
    }

    override fun updatePeerReview(id: String, peerReviewUpdateRq: PeerReviewUpdateRq): PeerReviewVO {
        peerReviewRepository.save(peerReviewRepository.findById(id).get().apply {
            gradesPerCriterions = peerReviewUpdateRq.gradesPerCriterions
            argumentsPerCriterions = peerReviewUpdateRq.argumentsPerCriterions
            summaryMessagePerSolution = peerReviewUpdateRq.summaryMessagePerSolution
            grade = peerReviewUpdateRq.grade!!
        }).id ?: throw IllegalArgumentException("Bad id returned.")
        log.debug("Updated task entity $id")
        return getPeerReviewByIdOrThrow(id)
    }

    private fun toPeerReviewVO(peerReview: PeerReview): PeerReviewVO {
        return PeerReviewVO.fromData(peerReview, authorizationService.currentUserIdOrDie())
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsByUserIdAndTaskId(userId: String, peerTaskId: String, pageRequest: PageRequest): Page<PeerReviewVO> {
        val student =  userRepository.findByIdOrNull(userId) ?: throw EntityNotFoundException("User with $userId not found")
        val peerTask = peerTaskRepository.findByIdOrNull(peerTaskId) ?: throw EntityNotFoundException("Task with $peerTaskId not found")

        val reviews = peerReviewRepository.findAllByStudentIdAndTaskIdOrderByCreatedDtDesc(student, peerTask, pageRequest)

        val receivedReviewsArray = mutableListOf<PeerReview>()
        receivedReviewsArray.groupBy { it.grade }.toList().forEach { (_, review) ->
            review.sortedByDescending { it.solutionId?.id }
            receivedReviewsArray.addAll(review)
        }
        return reviews.map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getAllReceivedReviewsByUserId(userId: String, pageRequest: PageRequest): Page<PeerReviewVO> {
        val student =  userRepository.findByIdOrNull(userId) ?: throw EntityNotFoundException("User with $userId not found")
        val reviews = peerReviewRepository.findAllByStudentIdOrderByCreatedDtDesc(student, pageRequest)

        val receivedReviewsArray = mutableListOf<PeerReview>()
        receivedReviewsArray.groupBy { it.grade }.toList().forEach { (_, review) ->
            review.sortedByDescending { it.solutionId?.id }
            receivedReviewsArray.addAll(review)
        }
        return reviews.map(::toPeerReviewVO)
    }
}