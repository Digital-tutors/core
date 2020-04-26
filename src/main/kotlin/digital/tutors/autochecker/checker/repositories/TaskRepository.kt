package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.vo.task.TaskVO
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface TaskRepository: MongoRepository<Task, String> {
    fun findByAuthorId(authorId: String): List<TaskVO>
    fun findByTopicId(topicId: String): List<TaskVO>
}