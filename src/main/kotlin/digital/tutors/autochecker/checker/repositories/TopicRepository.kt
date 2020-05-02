package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.checker.entities.Topic
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface TopicRepository : MongoRepository<Topic, String> {
    fun findAllByAccessTypeEquals(accessType: AccessType, pageable: Pageable): Page<Topic>
}
