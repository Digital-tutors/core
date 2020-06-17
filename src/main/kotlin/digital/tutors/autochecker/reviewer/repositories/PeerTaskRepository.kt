package digital.tutors.autochecker.reviewer.repositories

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.reviewer.entities.PeerTask
import digital.tutors.autochecker.checker.entities.Topic
import org.springframework.data.mongodb.repository.MongoRepository

    interface PeerTaskRepository : MongoRepository<PeerTask, String> {

        fun findAllByAuthorId(authorId: User): List<PeerTask>
        fun findAllByTopicId(topicId: Topic): List<PeerTask>
    }