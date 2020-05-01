package digital.tutors.autochecker.checker.services

import digital.tutors.autochecker.checker.vo.taskResults.TaskResultsCreateRq
import digital.tutors.autochecker.checker.vo.taskResults.TaskResultsVO
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.amqp.AmqpException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TaskResultsService {

    @Throws(EntityNotFoundException::class)
    fun getTaskResultsByAuthorId(authorId: String): List<TaskResultsVO>

    @Throws(EntityNotFoundException::class)
    fun getTaskResultsByTopicId(taskId: String): List<TaskResultsVO>

    @Throws(EntityNotFoundException::class)
    fun getTaskResultsByIdOrThrow(id: String): TaskResultsVO

    @Throws(EntityNotFoundException::class)
    fun getTaskResults(pageable: Pageable): Page<TaskResultsVO>

    @Throws(EntityNotFoundException::class, AmqpException::class)
    fun saveTaskResults(taskResultsCreateRq: TaskResultsCreateRq): TaskResultsVO

    @Throws(EntityNotFoundException::class)
    fun delete(id: String)


}
