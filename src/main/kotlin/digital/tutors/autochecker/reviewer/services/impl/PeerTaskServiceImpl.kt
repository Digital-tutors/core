package digital.tutors.autochecker.reviewer.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.reviewer.entities.PeerTask
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.reviewer.repositories.PeerTaskRepository
import digital.tutors.autochecker.reviewer.repositories.PeerTaskResultsRepository
import digital.tutors.autochecker.reviewer.services.PeerTaskService
import digital.tutors.autochecker.reviewer.vo.peerTask.PeerTaskAdminVO
import digital.tutors.autochecker.reviewer.vo.peerTask.PeerTaskCreateRq
import digital.tutors.autochecker.reviewer.vo.peerTask.PeerTaskUpdateRq
import digital.tutors.autochecker.reviewer.vo.peerTask.PeerTaskVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PeerTaskServiceImpl: PeerTaskService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var peerTaskRepository: PeerTaskRepository

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Autowired
    lateinit var peerTaskResultsRepository: PeerTaskResultsRepository

    @Throws(EntityNotFoundException::class)
    override fun getPeerTasksByAuthorId(authorId: String): List<PeerTaskVO> {
        return peerTaskRepository.findAllByAuthorId(User(id = authorId)).map(::toPeerTaskVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTasksByTopicId(topicId: String): List<PeerTaskVO> {
        return peerTaskRepository.findAllByTopicId(Topic(id = topicId)).map(::toPeerTaskVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTaskByIdOrThrow(id: String): PeerTaskVO {
        return peerTaskRepository.findById(id).map(::toPeerTaskVO).orElseThrow{ throw EntityNotFoundException("Task with $id not found") }
    }

    @Throws(EntityNotFoundException::class)
    override fun getAdminPeerTaskByIdOrThrow(id: String): PeerTaskAdminVO {
        return peerTaskRepository.findById(id).map(::toPeerTaskAdminVO).orElseThrow{ throw EntityNotFoundException("Task with $id not found") }
    }

    @Throws(EntityNotFoundException::class)
    override fun getPeerTasks(pageable: Pageable): Page<PeerTaskAdminVO> {
        return peerTaskRepository.findAll(pageable).map(::toPeerTaskAdminVO)
    }

    override fun createPeerTask(peerTaskCreateRq: PeerTaskCreateRq): PeerTaskVO {
        val id = peerTaskRepository.save(PeerTask().apply {
            topicId = Topic(id = peerTaskCreateRq.topicId?.id)
            authorId = User(id = peerTaskCreateRq.authorId?.id)
            contributors = peerTaskCreateRq.contributors?.map{ User(id = it.id)}
            level = peerTaskCreateRq.level
            description = peerTaskCreateRq.description
            options = peerTaskCreateRq.options
            title = peerTaskCreateRq.title
            criterions = peerTaskCreateRq.criterions
            maxGradesPerCriterions = peerTaskCreateRq.maxGradesPerCriterions
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Created entity $id")
        return getPeerTaskByIdOrThrow(id)
    }

    override fun updatePeerTask(id: String, peerTaskUpdateRq: PeerTaskUpdateRq): PeerTaskVO {
        peerTaskRepository.save(peerTaskRepository.findById(id).get().apply {
            topicId = Topic(id = peerTaskUpdateRq.topicId?.id)
            contributors = peerTaskUpdateRq.contributors?.map { User(id = it.id) }
            level = peerTaskUpdateRq.level
            description = peerTaskUpdateRq.description
            options = peerTaskUpdateRq.options
            title = peerTaskUpdateRq.title
            criterions = peerTaskUpdateRq.criterions
            maxGradesPerCriterions = peerTaskUpdateRq.maxGradesPerCriterions
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Updated task entity $id")
        return getPeerTaskByIdOrThrow(id)
    }

    override fun delete(id: String) {
        peerTaskRepository.deleteById(id)
        log.debug("Deleted task entity $id")
    }

    private fun toPeerTaskVO(peerTask: PeerTask): PeerTaskVO {
        val peerTaskResults = peerTaskResultsRepository.findFirstByStudentIdAndTaskIdAndCompletedIsTrue(
                User(id = authorizationService.currentUserIdOrDie()),
                PeerTask(id = peerTask.id)
        )

        return PeerTaskVO.fromData(peerTask, authorizationService.currentUserIdOrDie(), peerTaskResults?.completed != null)
    }

    private fun toPeerTaskAdminVO(peerTask: PeerTask): PeerTaskAdminVO {
        return PeerTaskAdminVO.fromData(peerTask)
    }
}