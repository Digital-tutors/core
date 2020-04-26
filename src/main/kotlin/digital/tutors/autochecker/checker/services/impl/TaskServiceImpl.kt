package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.checker.repositories.TaskRepository
import digital.tutors.autochecker.checker.services.TaskService
import digital.tutors.autochecker.checker.vo.task.TaskCreateRq
import digital.tutors.autochecker.checker.vo.task.TaskUpdateRq
import digital.tutors.autochecker.checker.vo.task.TaskVO
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.Tests
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TaskServiceImpl: TaskService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Throws(EntityNotFoundException::class)
    override fun getTasksByAuthorId(authorId: String): List<TaskVO> {
        return taskRepository.findByAuthorId(authorId)
    }

    @Throws(EntityNotFoundException::class)
    override fun getTaskByTopicId(topicId: String): List<TaskVO> {
        return taskRepository.findByTopicId(topicId)
    }

    @Throws(EntityNotFoundException::class)
    override fun getTaskByIdOrThrow(id: String): TaskVO {
        return taskRepository.findById(id).map(::toTaskVO).orElseThrow {
            throw EntityNotFoundException("Task with $id not found.")
        }
    }

    @Throws(EntityNotFoundException::class)
    override fun getTasks(pageable: Pageable): Page<TaskVO> {
        return taskRepository.findAll(pageable).map(::toTaskVO)
    }

    override fun createTask(taskCreateRq: TaskCreateRq): TaskVO {
        val id = taskRepository.save(Task().apply {
            description = taskCreateRq.description
            options = Options (
                    constructions = taskCreateRq.options?.constructions,
                    timeLimit = taskCreateRq.options?.timeLimit,
                    memoryLimit = taskCreateRq.options?.memoryLimit )

            tests = Tests (
                    input = taskCreateRq.tests?.input,
                    output = taskCreateRq.tests?.output  )

        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Created entity $id")
        return getTaskByIdOrThrow(id)
    }

    @Throws(EntityNotFoundException::class)
    override fun updateTask(id: String, taskUpdateRq: TaskUpdateRq): TaskVO {
        taskRepository.save(taskRepository.findById(id).get().apply {
            description = taskUpdateRq.description
            options = Options (
                    constructions = taskUpdateRq.options?.constructions,
                    timeLimit = taskUpdateRq.options?.timeLimit,
                    memoryLimit = taskUpdateRq.options?.memoryLimit
            )
            tests = Tests(
                    input = taskUpdateRq.tests?.input,
                    output = taskUpdateRq.tests?.output
            )
        }).id ?: throw IllegalArgumentException("Bad id returned.")
        log.debug("Updated task entity $id")
        return getTaskByIdOrThrow(id)
    }

    override fun delete(id: String) {
        taskRepository.deleteById(id)
        log.debug("Deleted task entity $id")
    }


    private fun toTaskVO(task: Task): TaskVO {
        return TaskVO.fromData(task)
    }

}