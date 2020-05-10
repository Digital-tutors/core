package digital.tutors.autochecker.checker.services.impl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.repositories.UserRepository
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.checker.entities.Status
import digital.tutors.autochecker.checker.entities.Task
import digital.tutors.autochecker.checker.entities.TaskResults
import digital.tutors.autochecker.checker.repositories.TaskRepository
import digital.tutors.autochecker.checker.repositories.TaskResultsRepository
import digital.tutors.autochecker.checker.services.TaskResultsService
import digital.tutors.autochecker.checker.vo.taskResults.TaskResultsCreateMQ
import digital.tutors.autochecker.checker.vo.taskResults.TaskResultsCreateRq
import digital.tutors.autochecker.checker.vo.taskResults.TaskResultsVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TaskResultsServiceImpl : TaskResultsService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var taskResultsRepository: TaskResultsRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    lateinit var authorizationService: AuthorizationService

    private val mapper = jacksonObjectMapper()

    @Throws(EntityNotFoundException::class)
    override fun getTaskResultsByAuthorId(authorId: String): List<TaskResultsVO> {
        return taskResultsRepository.findAllByUserId(User(id = authorId)).map(::toTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getTaskResultsByUserAndTask(userId: String, taskId: String): List<TaskResultsVO> {
        val user = userRepository.findByIdOrNull(userId)
                ?: throw EntityNotFoundException("User with $userId not found.")
        val task = taskRepository.findByIdOrNull(taskId)
                ?: throw EntityNotFoundException("Task with $taskId not found.")

        val taskResults = taskResultsRepository.findAllByUserIdAndTaskIdOrderByCreatedDtDesc(user, task)

        val taskResultsArray = mutableListOf<TaskResults>()
        taskResults.groupBy { it.language }.toList().forEach { (_, taskResult) ->
            taskResult.sortedByDescending { it.attempt }
            taskResultsArray.addAll(taskResult)
        }

        return taskResultsArray.map(::toTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getTaskResultsByUser(userId: String): List<TaskResultsVO> {
        val user = userRepository.findByIdOrNull(userId)
                ?: throw EntityNotFoundException("User with $userId not found.")
        val taskResults = taskResultsRepository.findAllByUserIdOrderByCreatedDtDesc(user)

        val taskResultsArray = mutableListOf<TaskResults>()
        taskResults.groupBy { it.language }.toList().forEach { (_, taskResult) ->
            taskResult.sortedByDescending { it.attempt }
            taskResultsArray.addAll(taskResult)
        }

        return taskResultsArray.map(::toTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getTaskResultsByTaskId(taskId: String): List<TaskResultsVO> {
        return taskResultsRepository.findAllByTaskId(Task(id = taskId)).map(::toTaskResultsVO)
    }

    @Throws(EntityNotFoundException::class)
    override fun getTaskResultsByIdOrThrow(id: String): TaskResultsVO = taskResultsRepository.findById(id).map(::toTaskResultsVO).orElseThrow { throw EntityNotFoundException("Task results with $id not found.") }

    @Throws(EntityNotFoundException::class)
    override fun getTaskResults(pageable: Pageable): Page<TaskResultsVO> {
        return taskResultsRepository.findAll(pageable).map(::toTaskResultsVO)
    }

    override fun saveTaskResults(taskResultsCreateRq: TaskResultsCreateRq): TaskResultsVO {
        val existingResults = taskResultsRepository.findFirstByUserIdAndTaskIdAndLanguageOrderByAttemptDesc(
                User(id = taskResultsCreateRq.userId?.id),
                Task(id = taskResultsCreateRq.taskId?.id),
                language = taskResultsCreateRq.language!!
        )

        val taskResultsId = taskResultsRepository.save(TaskResults().apply {
            taskId = Task(taskResultsCreateRq.taskId?.id)
            userId = User(taskResultsCreateRq.userId?.id)
            language = taskResultsCreateRq.language
            sourceCode = taskResultsCreateRq.sourceCode
            status = Status.RUNNING
        }.apply {
            if (existingResults != null) {
                attempt = existingResults.attempt + 1
            }
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        rabbitTemplate.convertSendAndReceive(
                "program",
                "program.tasks",
                mapper.writeValueAsString(TaskResultsCreateMQ(
                        taskResultsId,
                        taskResultsCreateRq.taskId,
                        taskResultsCreateRq.userId,
                        taskResultsCreateRq.language,
                        taskResultsCreateRq.sourceCode
                ))
        )

        log.debug("Created entity $taskResultsId")
        return getTaskResultsByIdOrThrow(taskResultsId)
    }

    override fun delete(id: String) {
        taskResultsRepository.deleteById(id)
        log.debug("Deleted task results entity $id")
    }

    private fun toTaskResultsVO(taskResults: TaskResults): TaskResultsVO {
        return TaskResultsVO.fromData(taskResults, authorizationService.currentUserIdOrDie())
    }

}
