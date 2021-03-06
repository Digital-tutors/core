package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.TaskResults
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface TaskResultsRepository : MongoRepository<TaskResults, String> {
    fun findAllByUserId(userId: User): List<TaskResults>
    fun findAllByUserIdOrderByCreatedDtDesc(userId: User, pageRequest: PageRequest): Page<TaskResults>
    fun findAllByTaskId(taskId: Task): List<TaskResults>
    fun findAllByTaskIdOrderByCreatedDtDesc(taskId: Task): List<TaskResults>
    fun findFirstByUserIdAndTaskId(userId: User, taskId: Task): TaskResults?
    fun findFirstByUserIdAndTaskIdAndCompletedIsTrue(userId: User, taskId: Task): TaskResults?
    fun findAllByUserIdAndTaskId(userId: User, taskId: Task, pageRequest: PageRequest): Page<TaskResults>
    fun findAllByUserIdAndTaskIdOrderByCreatedDtDesc(userId: User, taskId: Task, pageRequest: PageRequest): Page<TaskResults>
    fun findFirstByUserIdAndTaskIdAndLanguageOrderByAttemptDesc(userId: User, taskId: Task, language: String): TaskResults?
    fun findFirstById(id: String): TaskResults
}
