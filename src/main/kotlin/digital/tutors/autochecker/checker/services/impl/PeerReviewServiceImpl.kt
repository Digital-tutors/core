package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.checker.entities.PeerReview
import digital.tutors.autochecker.checker.entities.PeerTask
import digital.tutors.autochecker.checker.entities.PeerTaskSolution
import digital.tutors.autochecker.checker.repositories.PeerReviewRepository
import digital.tutors.autochecker.checker.services.PeerReviewService
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewCreateRq
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewUpdateRq
import digital.tutors.autochecker.checker.vo.peerReview.PeerReviewVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

class PeerReviewServiceImpl: PeerReviewService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var peerReviewRepository: PeerReviewRepository

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsByExpertId(expertId: String): List<PeerReviewVO> {
        return peerReviewRepository.findAllByExpertId(User(id = expertId)).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsByStudentId(studentId: String): List<PeerReviewVO> {
        return peerReviewRepository.findAllByStudentId(User(id = studentId)).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsByPeerTaskId(taskId: String): List<PeerReviewVO> {
        return peerReviewRepository.findAllByTaskId(PeerTask(id = taskId)).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewsBySolutionId(solutionId: String): List<PeerReviewVO> {
        return peerReviewRepository.findAllBySolutionId(PeerTaskSolution(id = solutionId)).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviewByIdOrThrow(id: String): PeerReviewVO {
        return peerReviewRepository.findById(id).map(::toPeerReviewVO).orElseThrow{ throw EntityNotFoundException("Peer review with $id not found") }
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerReviews(pageable: Pageable): Page<PeerReviewVO> {
        return peerReviewRepository.findAll(pageable).map(::toPeerReviewVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun createPeerReview(peerReviewCreateRq: PeerReviewCreateRq): PeerReviewVO {
        val id = peerReviewRepository.save(PeerReview().apply {
            taskId = PeerTask(id = peerReviewCreateRq.taskId?.id)
            studentId = User(id = peerReviewCreateRq.studentId?.id)
            expertId = User(id = peerReviewCreateRq.expertId?.id)
            solutionId = PeerTaskSolution(id = peerReviewCreateRq.solutionId?.id)
            argumentsPerCriterions = peerReviewCreateRq.argumentsPerCriterions
            gradesPerCriterions = peerReviewCreateRq.gradesPerCriterions
            summaryMessagePerSolution = peerReviewCreateRq.summaryMessagePerSolution
        }).id ?: throw IllegalArgumentException("Bad id returned.")
        TODO("DATE")
        log.debug("Created entity $id")
        return getPeerReviewByIdOrThrow(id)
    }

    @Throws(EntityNotFoundException::class)
    override fun updatePeerReview(id: String, peerReviewUpdateRq: PeerReviewUpdateRq): PeerReviewVO {
        peerReviewRepository.save(peerReviewRepository.findById(id).get().apply {
            gradesPerCriterions = peerReviewUpdateRq.gradesPerCriterions
            argumentsPerCriterions = peerReviewUpdateRq.argumentsPerCriterions
            summaryMessagePerSolution = peerReviewUpdateRq.summaryMessagePerSolution
        }).id ?: throw IllegalArgumentException("Bad id returned.")
        TODO("DATE")
        log.debug("Updated task entity $id")
        return getPeerReviewByIdOrThrow(id)
    }

    private fun toPeerReviewVO(peerReview: PeerReview): PeerReviewVO {
        return PeerReviewVO.fromData(peerReview, authorizationService.currentUserIdOrDie())
    }

}