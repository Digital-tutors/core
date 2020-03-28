package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.checker.entities.Topic
import org.springframework.data.mongodb.repository.MongoRepository

interface TopicRepository : MongoRepository<Topic, String>