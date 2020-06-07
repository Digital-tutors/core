package digital.tutors.autochecker.reviewer.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.reviewer.entities.PeerReview
import digital.tutors.autochecker.reviewer.entities.PeerTask
import digital.tutors.autochecker.reviewer.entities.PeerTaskResults
import digital.tutors.autochecker.reviewer.repositories.PeerTaskResultsRepository
import digital.tutors.autochecker.reviewer.services.PeerTaskResultsService
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsCreateRq
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsUpdateRq
import digital.tutors.autochecker.reviewer.vo.peerTaskResults.PeerTaskResultsVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PeerTaskResultsServiceImpl: PeerTaskResultsService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var peerTaskResultsRepository: PeerTaskResultsRepository

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResultsByUserAndTask(userId: String, taskId: String): List<PeerTaskResultsVO> {
        return peerTaskResultsRepository.findAllByStudentIdAndTaskId(User(id = userId), PeerTask(id = taskId)).map(::toPeerTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResultsByUser(userId: String): List<PeerTaskResultsVO> {
        return peerTaskResultsRepository.findAllByStudentId(User(id = userId)).map(::toPeerTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResultsByTaskId(taskId: String): List<PeerTaskResultsVO> {
        return  peerTaskResultsRepository.findAllByTaskId(PeerTask(id = taskId)).map(::toPeerTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResultsByIdOrThrow(id: String): PeerTaskResultsVO {
        return peerTaskResultsRepository.findById(id).map(::toPeerTaskResultsVO).orElseThrow { throw EntityNotFoundException("Task results with $id not found") }
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskResults(pageable: Pageable): Page<PeerTaskResultsVO> {
        return peerTaskResultsRepository.findAll(pageable).map(::toPeerTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun createPeerTaskResults(peerTaskResultsCreateRq: PeerTaskResultsCreateRq): PeerTaskResultsVO {
        val id = peerTaskResultsRepository.save(PeerTaskResults().apply {
            taskId = PeerTask(id = peerTaskResultsCreateRq.taskId?.id)
            studentId = User(id = peerTaskResultsCreateRq.studentId?.id)
            receivedReviews = peerTaskResultsCreateRq.receivedReviews?.map { PeerReview(id = it.id) }
            postedReviews = peerTaskResultsCreateRq.receivedReviews?.map { PeerReview(id = it.id) }
            grade = peerTaskResultsCreateRq.grade
            completed = peerTaskResultsCreateRq.completed
            status = peerTaskResultsCreateRq.status
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        TODO("DATE")
        log.debug("Created entity $id")
        return getPeerTaskResultsByIdOrThrow(id)
    }

    @Throws(EntityNotFoundException::class)
    override fun updatePeerTaskResults(id: String, peerTaskResultsUpdateRq: PeerTaskResultsUpdateRq): PeerTaskResultsVO {
        peerTaskResultsRepository.save(peerTaskResultsRepository.findById(id).get().apply {
            receivedReviews = peerTaskResultsUpdateRq.receivedReviews?.map { PeerReview(id = it.id) }
            postedReviews = peerTaskResultsUpdateRq.postedReviews?.map { PeerReview(id = it.id) }
            grade = peerTaskResultsUpdateRq.grade
            completed = peerTaskResultsUpdateRq.completed
            status = peerTaskResultsUpdateRq.status
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        TODO("DATE")
        log.debug("Created entity $id")
        return getPeerTaskResultsByIdOrThrow(id)
    }

    private fun toPeerTaskResultsVO(peerTaskResults: PeerTaskResults): PeerTaskResultsVO {
        return PeerTaskResultsVO.fromData(peerTaskResults, authorizationService.currentUserIdOrDie(), peerTaskResults.completed)
    }
}