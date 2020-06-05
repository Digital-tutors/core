package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.checker.entities.PeerReview
import digital.tutors.autochecker.checker.entities.PeerTask
import digital.tutors.autochecker.checker.entities.PeerTaskSolution
import org.springframework.data.mongodb.repository.MongoRepository

interface PeerReviewRepository : MongoRepository<PeerReview, String> {
    fun findAllByStudentId(studentId: User): List<PeerReview>
    fun findAllByExpertId(expertId: User): List<PeerReview>
    fun findAllBySolutionId(solutionId: PeerTaskSolution): List<PeerReview>
    fun findAllByTaskId(taskId: PeerTask): List<PeerReview>
    fun findFirstById(id: String): PeerReview
    fun findFirstByStudentId(studentId: User): PeerReview
    fun findFirstByExpertId(expertId: User): PeerReview
    fun findAllByStudentIdOrderByCreatedAtDesc(studentId: User): List<PeerReview>
}