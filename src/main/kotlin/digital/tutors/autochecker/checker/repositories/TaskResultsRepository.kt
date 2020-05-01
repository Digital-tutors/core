package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.TaskResults
import org.springframework.data.mongodb.repository.MongoRepository

interface TaskResultsRepository : MongoRepository<TaskResults, String> {
    fun findAllByUserId(userId: User): List<TaskResults>
    fun findAllByTaskId(taskId: Task): List<TaskResults>
    fun findFirstByUserIdAndTaskId(userId: User, taskId: Task): TaskResults?
    fun findFirstById(id: String): TaskResults
}
