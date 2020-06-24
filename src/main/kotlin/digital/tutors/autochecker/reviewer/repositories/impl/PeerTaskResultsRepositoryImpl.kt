package digital.tutors.autochecker.reviewer.repositories.impl

import digital.tutors.autochecker.reviewer.entities.PeerTaskResults
import digital.tutors.autochecker.reviewer.entities.PeerTaskResultsStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository


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

}