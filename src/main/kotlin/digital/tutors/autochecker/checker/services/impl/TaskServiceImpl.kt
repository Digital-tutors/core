package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.checker.repositories.TaskRepository
import digital.tutors.autochecker.checker.services.TaskService
import digital.tutors.autochecker.checker.vo.task.TaskCreateRq
import digital.tutors.autochecker.checker.vo.task.TaskUpdateRq
import digital.tutors.autochecker.checker.vo.task.TaskVO
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TaskServiceImpl : TaskService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Throws(EntityNotFoundException::class)
    override fun getTasksByAuthorId(authorId: String): List<TaskVO> {
        return taskRepository.findAllByAuthorId(User(id = authorId))
    }

    @Throws(EntityNotFoundException::class)
    override fun getTaskByTopicId(topicId: String): List<TaskVO> {
        return taskRepository.findAllByTopicId(Topic(id = topicId))
    }

    @Throws(EntityNotFoundException::class)
    override fun getTaskByIdOrThrow(id: String): TaskVO = taskRepository.findById(id).map(::toTaskVO).orElseThrow { throw EntityNotFoundException("Task with $id not found.") }

    @Throws(EntityNotFoundException::class)
    override fun getTasks(pageable: Pageable): Page<TaskVO> {
        return taskRepository.findAll(pageable).map(::toTaskVO)
    }

    override fun createTask(taskCreateRq: TaskCreateRq): TaskVO {
        val id = taskRepository.save(Task().apply {
            topicId = Topic(id = taskCreateRq.topicId?.id)
            authorId = User(id = taskCreateRq.authorId?.id)
            contributors = taskCreateRq.contributors?.map { User(id = it.id) }
            level = taskCreateRq.level
            description = taskCreateRq.description
            options = taskCreateRq.options
            test = taskCreateRq.test
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Created entity $id")
        return getTaskByIdOrThrow(id)
    }

    @Throws(EntityNotFoundException::class)
    override fun updateTask(id: String, taskUpdateRq: TaskUpdateRq): TaskVO {
        taskRepository.save(taskRepository.findById(id).get().apply {
            topicId = Topic(id = taskUpdateRq.topicId?.id)
            contributors = taskUpdateRq.contributors?.map { User(id = it.id) }
            level = taskUpdateRq.level
            description = taskUpdateRq.description
            options = taskUpdateRq.options
            test = taskUpdateRq.test
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
