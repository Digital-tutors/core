package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.checker.vo.task.TaskVO
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface TaskRepository : MongoRepository<Task, String> {
    fun findAllByAuthorId(authorId: User): List<Task>
    fun findAllByTopicId(topicId: Topic): List<Task>
}
