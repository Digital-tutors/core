package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.repositories.UserRepository
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.checker.repositories.TaskRepository
import digital.tutors.autochecker.checker.services.TaskService
import digital.tutors.autochecker.checker.vo.task.TaskCreateRq
import digital.tutors.autochecker.checker.vo.task.TaskUpdateRq
import digital.tutors.autochecker.checker.vo.task.TaskVO
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.checker.repositories.TaskResultsRepository
import digital.tutors.autochecker.checker.repositories.TopicRepository
import digital.tutors.autochecker.checker.vo.task.TaskAdminVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TaskServiceImpl : TaskService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var topicRepository: TopicRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var taskResultsRepository: TaskResultsRepository

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Throws(EntityNotFoundException::class)
    override fun getTasksByAuthorId(authorId: String): List<TaskVO> {
        return taskRepository.findAllByAuthorId(User(id = authorId)).map(::toTaskVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getTasksByTopicId(topicId: String): List<TaskVO> {
        return taskRepository.findAllByTopicId(Topic(id = topicId)).map(::toTaskVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getTaskByIdOrThrow(id: String): TaskVO = taskRepository.findById(id).map(::toTaskVO).orElseThrow { throw EntityNotFoundException("Task with $id not found.") }

    @Throws(EntityNotFoundException::class)
    override fun getAdminTaskByIdOrThrow(id: String): TaskAdminVO = taskRepository.findById(id).map(::toTaskAdminVO).orElseThrow { throw EntityNotFoundException("Task with $id not found.") }

    @Throws(EntityNotFoundException::class)
    override fun getTasks(pageable: Pageable): Page<TaskAdminVO> {
        return taskRepository.findAll(pageable).map(::toTaskAdminVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun createTask(taskCreateRq: TaskCreateRq): TaskVO {
        val user = userRepository.findByIdOrNull(authorizationService.currentUserIdOrDie())
                ?: throw EntityNotFoundException("User with ${authorizationService.currentUserIdOrDie()} not found.")

        val topic = topicRepository.findByIdOrNull(taskCreateRq.topicId?.id)
                ?: throw EntityNotFoundException("Topic with ${taskCreateRq.topicId?.id} not found.")

        val id = taskRepository.save(Task().apply {
            topicId = topic
            authorId = user
            contributors = taskCreateRq.contributors?.map { User(id = it.id) }
            level = taskCreateRq.level
            description = taskCreateRq.description
            options = taskCreateRq.options
            title = taskCreateRq.title
            tests = taskCreateRq.tests
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
            title = taskUpdateRq.title
            tests = taskUpdateRq.tests
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Updated task entity $id")
        return getTaskByIdOrThrow(id)
    }

    override fun delete(id: String) {
        taskRepository.deleteById(id)
        log.debug("Deleted task entity $id")
    }

    private fun toTaskVO(task: Task): TaskVO {
        val taskResults = taskResultsRepository.findFirstByUserIdAndTaskIdAndCompletedIsTrue(
                User(id = authorizationService.currentUserIdOrDie()),
                Task(id = task.id)
        )

        return TaskVO.fromData(task, authorizationService.currentUserIdOrDie(), taskResults?.completed != null)
    }

    private fun toTaskAdminVO(task: Task): TaskAdminVO {
        return TaskAdminVO.fromData(task)
    }


}
