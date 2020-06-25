package digital.tutors.autochecker.reviewer.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.repositories.UserRepository
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
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
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PeerReviewServiceImpl: PeerReviewService {

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
        return peerReviewRepository.findAllByStudentId(student).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsByPeerTaskId(taskId: String): List<PeerReviewVO> {
        val task = peerTaskRepository.findByIdOrNull(taskId) ?: throw EntityNotFoundException("Task with $taskId not found.")

        return peerReviewRepository.findAllByTaskId(task).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsBySolutionId(solutionId: String): List<PeerReviewVO> {
        val solution = peerTaskSolutionRepository.findByIdOrNull(solutionId) ?: throw EntityNotFoundException("Solution with $solutionId not found.")
        return peerReviewRepository.findAllBySolutionId(solution).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewByIdOrThrow(id: String): PeerReviewVO {
        return peerReviewRepository.findById(id).map(::toPeerReviewVO).orElseThrow{ throw EntityNotFoundException("Peer review with $id not found") }
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviews(pageable: Pageable): Page<PeerReviewVO> {
        return peerReviewRepository.findAll(pageable).map(::toPeerReviewVO)
    }

    @Transactional
    override fun createPeerReview(peerReviewCreateRq: PeerReviewCreateRq): PeerReviewVO {
        val id = peerReviewRepository.save(PeerReview().apply {
            taskId = PeerTask(peerReviewCreateRq.taskId?.id)
            studentId = User(peerReviewCreateRq.studentId?.id)
            expertId = User(peerReviewCreateRq.expertId?.id)
            solutionId = PeerTaskSolution(peerReviewCreateRq.solutionId?.id)
            argumentsPerCriterions = peerReviewCreateRq.argumentsPerCriterions
            gradesPerCriterions = peerReviewCreateRq.gradesPerCriterions
            summaryMessagePerSolution = peerReviewCreateRq.summaryMessagePerSolution
            grade = peerReviewCreateRq.grade!!
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        val studentResult = peerTaskResultsRepository.findFirstByStudentIdAndTaskId(User(peerReviewCreateRq.studentId?.id), PeerTask(peerReviewCreateRq.taskId?.id))
        val expertResult = peerTaskResultsRepository.findFirstByStudentIdAndTaskId(User(peerReviewCreateRq.expertId?.id), PeerTask(peerReviewCreateRq.taskId?.id))
        var gradesArray: MutableList<Double> = arrayListOf()

        if(expertResult!!.postedReviews + 1 == 3 && expertResult!!.receivedReviews >= 3) {
            val reviewsArray = peerReviewRepository.findAllByStudentIdAndTaskIdOrderByCreatedDtAsc(User(peerReviewCreateRq.studentId?.id), PeerTask(peerReviewCreateRq.taskId?.id)).subList(0, 3)

            for((index, review) in reviewsArray.withIndex()) {
                gradesArray[index] = review.grade
            }
        }
        peerTaskResultsRepositoryImpl.run {
            updateValueOfReceivedReviewsAndStatus(studentResult?.receivedReviews!! + 1, PeerTaskResultsStatus.NOT_CHECKING, studentResult.id!!)
            updateValueOfPostedReviews(expertResult!!.postedReviews + 1, expertResult.id!!, gradesArray)
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

}