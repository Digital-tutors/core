package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.checker.services.PeerTaskResultsService
import digital.tutors.autochecker.checker.vo.peerTaskResults.PeerTaskResultsCreateRq
import digital.tutors.autochecker.checker.vo.peerTaskResults.PeerTaskResultsVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class PeerTaskResultsServiceImpl: PeerTaskResultsService {
    override fun getPeerTaskResultsByAuthorId(authorId: String): List<PeerTaskResultsVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerTaskResultsByUserAndTask(userId: String, taskId: String): List<PeerTaskResultsVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerTaskResultsByUser(userId: String): List<PeerTaskResultsVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerTaskResultsByTaskId(taskId: String): List<PeerTaskResultsVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerTaskResultsByIdOrThrow(id: String): PeerTaskResultsVO {
        TODO("Not yet implemented")
    }

    override fun getPeerTaskResults(pageable: Pageable): Page<PeerTaskResultsVO> {
        TODO("Not yet implemented")
    }

    override fun savePeerTaskResults(peerTaskResultsCreateRq: PeerTaskResultsCreateRq): PeerTaskResultsVO {
        TODO("Not yet implemented")
    }

    override fun delete(id: String) {
        TODO("Not yet implemented")
    }
}