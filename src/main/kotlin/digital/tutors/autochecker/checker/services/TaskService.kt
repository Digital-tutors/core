package digital.tutors.autochecker.checker.services

import digital.tutors.autochecker.checker.vo.task.TaskCreateRq
import digital.tutors.autochecker.checker.vo.task.TaskUpdateRq
import digital.tutors.autochecker.checker.vo.task.TaskVO
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TaskService {

    @Throws(EntityNotFoundException::class)
    fun getTasksByAuthorId(authorId: String): List<TaskVO>

    @Throws(EntityNotFoundException::class)
    fun getTaskByTopicId(id: String): List<TaskVO>

    @Throws(EntityNotFoundException::class)
    fun getTaskByIdOrThrow(id: String): TaskVO

    @Throws(EntityNotFoundException::class)
    fun getTasks(pageable: Pageable): Page<TaskVO>

    @Throws(EntityNotFoundException::class)
    fun createTask(taskCreateRq: TaskCreateRq): TaskVO

    @Throws(EntityNotFoundException::class)
    fun updateTask(id: String, taskUpdateRq: TaskUpdateRq): TaskVO

    @Throws(EntityNotFoundException::class)
    fun delete(id: String)


}