package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.checker.entities.PeerTask
import digital.tutors.autochecker.checker.entities.PeerTaskResults
import org.springframework.data.mongodb.repository.MongoRepository

interface PeerTaskResultsRepository : MongoRepository<PeerTaskResults, String> {

    fun findAllByStudentId(userId: User): List<PeerTaskResults>
    fun findAllByStudentIdOrderByCreatedDtDesc(userId: User): List<PeerTaskResults>
    fun findAllByTaskId(taskId: PeerTask): List<PeerTaskResults>
    fun findAllByTaskIdOrderByCreatedDtDesc(taskId: PeerTask): List<PeerTaskResults>
    fun findAllByTaskIdAndCompletedIsTrue(taskId: PeerTask): List<PeerTaskResults>
    fun findFirstByStudentIdAndTaskId(userId: User, taskId: PeerTask): PeerTaskResults?
    fun findFirstByStudentIdAndTaskIdAndCompletedIsTrue(userId: User, taskId: PeerTask): PeerTaskResults?
    fun findAllByStudentIdAndTaskId(userId: User, taskId: PeerTask): List<PeerTaskResults>
    fun findAllByStudentIdAndTaskIdOrderByCreatedDtDesc(userId: User, taskId: PeerTask): List<PeerTaskResults>
    fun findFirstById(id: String): PeerTaskResults

}