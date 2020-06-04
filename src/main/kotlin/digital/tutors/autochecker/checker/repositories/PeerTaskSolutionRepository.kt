package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.checker.entities.PeerTask
import digital.tutors.autochecker.checker.entities.PeerTaskSolution
import org.springframework.data.mongodb.repository.MongoRepository

interface PeerTaskSolutionRepository: MongoRepository<PeerTaskSolution, String> {
    fun findAllByUserId(userId: User): List<PeerTaskSolution>
    fun findAllByUserIdOrderByCreatedDtDesc(userId: User): List<PeerTaskSolution>
    fun findAllByTaskId(taskId: PeerTask): List<PeerTaskSolution>
    fun findAllByTaskIdOrderByCreatedDtDesc(taskId: PeerTask): List<PeerTaskSolution>
    fun findFirstByUserIdAndTaskId(userId: User, taskId: PeerTask): PeerTaskSolution?
    fun findFirstByUserIdAndTaskIdAndCompletedIsTrue(userId: User, taskId: PeerTask): PeerTaskSolution?
    fun findAllByUserIdAndTaskId(userId: User, taskId: PeerTask): List<PeerTaskSolution>
    fun findAllByUserIdAndTaskIdOrderByCreatedDtDesc(userId: User, taskId: PeerTask): List<PeerTaskSolution>
    fun findFirstByUserIdAndTaskIdAndLanguageOrderByAttemptDesc(userId: User, taskId: PeerTask, language: String): PeerTaskSolution?
    fun findFirstById(id: String): PeerTaskSolution
}