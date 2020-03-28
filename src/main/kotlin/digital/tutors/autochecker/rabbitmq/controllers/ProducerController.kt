package digital.tutors.autochecker.rabbitmq.controllers

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ProducerController {

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    data class Test(
            val name: String,
            val text: String
    )

    @PostMapping("/person")
    fun postPerson(@RequestBody body: Test): ResponseEntity<Test> {
//        rabbitTemplate.sendAndReceive(RabbitmqConfiguration().topicExchangeName, RabbitmqConfiguration().routingKey, Message(
//                ObjectMapper().writeValueAsBytes(body),
//                MessageProperties().apply {
//                    deliveryMode = MessageDeliveryMode.PERSISTENT
//                    contentType = MessageProperties.CONTENT_TYPE_JSON
//                    replyTo = "Test"
//                    correlationId = "MyTest123"
//                }
//        ))

        rabbitTemplate.convertSendAndReceive("program", "program.tasks", body)
        return ResponseEntity.ok(body)
    }

}
