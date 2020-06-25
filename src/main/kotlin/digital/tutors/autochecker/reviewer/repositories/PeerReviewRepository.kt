package digital.tutors.autochecker.reviewer.repositories

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.reviewer.entities.PeerReview
import digital.tutors.autochecker.reviewer.entities.PeerTask
import digital.tutors.autochecker.reviewer.entities.PeerTaskSolution
import org.springframework.data.mongodb.repository.MongoRepository

interface PeerReviewRepository : MongoRepository<PeerReview, String> {

    fun findAllByStudentId(studentId: User): List<PeerReview>
    fun findAllByExpertId(expertId: User): List<PeerReview>
    fun findAllBySolutionId(solutionId: PeerTaskSolution): List<PeerReview>
    fun findAllByTaskId(taskId: PeerTask): List<PeerReview>
    fun findFirstById(id: String): PeerReview
    fun findFirstByStudentId(studentId: User): PeerReview
    fun findFirstByExpertId(expertId: User): PeerReview
    fun findAllByStudentIdAndTaskIdOrderByCreatedDtAsc(studentId: User, peerTaskId: PeerTask): List<PeerReview>
}