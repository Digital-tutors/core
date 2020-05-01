package digital.tutors.autochecker.rabbitmq

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.checker.entities.Status
import digital.tutors.autochecker.checker.repositories.TaskResultsRepository
import digital.tutors.autochecker.checker.vo.taskResults.TaskResultsMQ
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class TaskResultsConsumer {

    @Autowired
    lateinit var taskResultsRepository: TaskResultsRepository

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    private val latch = CountDownLatch(1)
    private val mapper = jacksonObjectMapper()

    @RabbitListener(queues = ["program.results"])
    fun receiveMessage(message: String) {
        val taskResults = mapper.readValue(message, TaskResultsMQ::class.java)

        val id = taskResultsRepository.save(taskResultsRepository.findFirstById(taskResults.id).apply {
            completed = taskResults.completed
            codeReturn = taskResults.codeReturn
            messageOut = taskResults.messageOut
            runtime = taskResults.runtime
            memory = taskResults.memory
            status = Status.COMPLETED
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Updated entity $id")
        latch.countDown()
    }

}
