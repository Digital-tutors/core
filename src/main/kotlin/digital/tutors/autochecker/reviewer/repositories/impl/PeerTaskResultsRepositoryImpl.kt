package digital.tutors.autochecker.reviewer.repositories.impl

import digital.tutors.autochecker.reviewer.entities.PeerTaskResults
import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import java.util.*


@Repository
class PeerTaskResultsRepositoryImpl {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    fun setStatusForPeerTaskResults(status: PeerTaskResultsStatus, id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update
                .update("status", status)

        this.mongoTemplate.findAndModify(query, update, PeerTaskResults::class.java)
    }

    fun updateValueOfPostedReviews(value: Int, id: String, grades: MutableList<Double>?) {

        var grade: Double = 0.0

        if(!grades.isNullOrEmpty()) {
            grade = getMedianOfNumber(grades!!)
        }

        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update
                .update("postedReviews", value)
                .set("grade", grade)

        this.mongoTemplate.findAndModify(query, update, PeerTaskResults::class.java)

    }

    fun updateValueOfReceivedReviews(value: Int, id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update
                .update("receivedReviews", value)

        this.mongoTemplate.findAndModify(query, update, PeerTaskResults::class.java)

    }

    fun updateValueOfReceivedReviewsAndStatus(value: Int, status: PeerTaskResultsStatus, id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update
                .update("receivedReviews", value)
                .set("status", status)

        this.mongoTemplate.findAndModify(query, update, PeerTaskResults::class.java)

    }

    private fun getMedianOfNumber(arrayForFindingTheMedianOfNumber: MutableList<Double>): Double
    {
        arrayForFindingTheMedianOfNumber.sort()
        return if (arrayForFindingTheMedianOfNumber.size % 2 == 0) {
            (arrayForFindingTheMedianOfNumber[arrayForFindingTheMedianOfNumber.size / 2] + arrayForFindingTheMedianOfNumber[arrayForFindingTheMedianOfNumber.size / 2 - 1]) / 2f
        } else arrayForFindingTheMedianOfNumber[arrayForFindingTheMedianOfNumber.size / 2].toDouble()
    }

}