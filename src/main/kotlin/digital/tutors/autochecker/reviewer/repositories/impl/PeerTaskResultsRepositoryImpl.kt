package digital.tutors.autochecker.reviewer.repositories.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.reviewer.entities.PeerReview
import digital.tutors.autochecker.reviewer.entities.PeerTask
import digital.tutors.autochecker.reviewer.entities.PeerTaskResults
import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import digital.tutors.autochecker.reviewer.repositories.PeerReviewRepository
import digital.tutors.autochecker.reviewer.repositories.PeerTaskResultsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository


@Repository
class PeerTaskResultsRepositoryImpl {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var peerReviewRepository: PeerReviewRepository

    fun setStatusForPeerTaskResults(status: PeerTaskResultsStatus, id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update
                .update("status", status)

        this.mongoTemplate.findAndModify(query, update, PeerTaskResults::class.java)
    }

    fun updateFields(userId: User, isPostedReviews: Boolean, isReceivedReviews: Boolean, peerTask: PeerTask) {
        val findResultQuery = Query(Criteria.where("studentId").`is`(userId.id))
        val result = this.mongoTemplate.findOne(findResultQuery, PeerTaskResults::class.java)

        var grade: Double = 0.0
        var status: PeerTaskResultsStatus = PeerTaskResultsStatus.NOT_CHECKING
        var completed = result?.completed
        var postedReviews = result?.postedReviews
        var receivedReviews = result?.receivedReviews

        if(isPostedReviews) {
            postedReviews = postedReviews?.plus(1)
        }

        if(isReceivedReviews) {
            receivedReviews = receivedReviews?.plus(1)
        }

        if(postedReviews!! >= 3 && receivedReviews!! >= 3 && !completed!!) {
            completed = true
            status = PeerTaskResultsStatus.COMPLETED
            grade = getMedianOfNumber(peerReviewRepository.findAllByStudentIdAndTaskIdOrderByCreatedDtAsc(userId, peerTask).map { it -> it.grade }.subList(0, 3).toMutableList())
        }

        val updateQuery = Query(Criteria.where("studentId").`is`(userId.id).and("taskId").`is`(peerTask.id))
        val update = Update
                .update("postedReviews", postedReviews)
                .set("receivedReviews", receivedReviews!!)
                .set("grade", grade)
                .set("completed", completed!!)
                .set("status", status)

        this.mongoTemplate.findAndModify(updateQuery, update, PeerTaskResults::class.java)
    }


    private fun getMedianOfNumber(arrayForFindingTheMedianOfNumber: MutableList<Double>): Double
    {
        arrayForFindingTheMedianOfNumber.sort()
        return if (arrayForFindingTheMedianOfNumber.size % 2 == 0) {
            (arrayForFindingTheMedianOfNumber[arrayForFindingTheMedianOfNumber.size / 2] + arrayForFindingTheMedianOfNumber[arrayForFindingTheMedianOfNumber.size / 2 - 1]) / 2f
        } else arrayForFindingTheMedianOfNumber[arrayForFindingTheMedianOfNumber.size / 2].toDouble()
    }

     fun getRandomUserResult(status: PeerTaskResultsStatus, peerTaskId: PeerTask, studentId: User, studentList: List<User>): PeerTaskResults? {

        val query =
            Query(Criteria.where("completed").`is`(false).and("status").`is`(status).and("taskId").`is`(peerTaskId).and("studentId").nin(studentList))

         query.with(Sort.by(Sort.Direction.DESC, "createdDt"))

         return this.mongoTemplate.findOne(query, PeerTaskResults::class.java)
    }

}