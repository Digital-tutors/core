package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.checker.services.PeerTaskService
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskAdminVO
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskCreateRq
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskUpdateRq
import digital.tutors.autochecker.checker.vo.peerTask.PeerTaskVO
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class PeerTaskServiceImpl: PeerTaskService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override fun getPeerTasksByAuthorId(authorId: String): List<PeerTaskVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerTasksByTopicId(topicId: String): List<PeerTaskVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerTaskByIdOrThrow(id: String): PeerTaskVO {
        TODO("Not yet implemented")
    }

    override fun getAdminPeerTaskByIdOrThrow(id: String): PeerTaskAdminVO {
        TODO("Not yet implemented")
    }

    override fun getPeerTasks(pageable: Pageable): Page<PeerTaskAdminVO> {
        TODO("Not yet implemented")
    }

    override fun createPeerTask(peerTaskCreateRq: PeerTaskCreateRq): PeerTaskVO {
        TODO("Not yet implemented")
    }

    override fun updatePeerTask(id: String, peerTaskUpdateRq: PeerTaskUpdateRq): PeerTaskVO {
        TODO("Not yet implemented")
    }

    override fun delete(id: String) {
        TODO("Not yet implemented")
    }
}