package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.checker.services.PeerTaskSolutionService
import digital.tutors.autochecker.checker.vo.peerTaskSolution.PeerTaskSolutionCreateRq
import digital.tutors.autochecker.checker.vo.peerTaskSolution.PeerTaskSolutionVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class PeerTaskSolutionServiceImpl: PeerTaskSolutionService {
    override fun getPeerTaskSolutionsByUserAndTask(userId: String, taskId: String): List<PeerTaskSolutionVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerTaskSolutionsByUser(userId: String): List<PeerTaskSolutionVO> {
        TODO("Not yet implemented")
    }

    override fun getPeerTaskSolutionsByTaskId(taskId: String): List<PeerTaskSolutionVO> {
        TODO("Not yet implemented")
    }

    override fun getFirstPeerTaskSolutionByUserAndTask(userId: String, taskId: String): PeerTaskSolutionVO {
        TODO("Not yet implemented")
    }

    override fun getFirstPeerTaskSolutionByUser(userId: String): PeerTaskSolutionVO {
        TODO("Not yet implemented")
    }

    override fun getFirstPeerTaskSolutionByTaskId(taskId: String): PeerTaskSolutionVO {
        TODO("Not yet implemented")
    }

    override fun getPeerTaskSolutionByIdOrThrow(id: String): PeerTaskSolutionVO {
        TODO("Not yet implemented")
    }

    override fun getPeerTaskSolutions(pageable: Pageable): Page<PeerTaskSolutionVO> {
        TODO("Not yet implemented")
    }

    override fun savePeerTaskSolution(peerTaskSolutionCreateRq: PeerTaskSolutionCreateRq): PeerTaskSolutionVO {
        TODO("Not yet implemented")
    }

    override fun delete(id: String) {
        TODO("Not yet implemented")
    }
}