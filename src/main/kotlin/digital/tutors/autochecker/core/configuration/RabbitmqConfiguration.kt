package digital.tutors.autochecker.core.configuration

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqConfiguration {

    @Bean
    fun autocheckerBindings(): Declarables {
        val tasksQueue = Queue("program.tasks", true)
        val resultsQueue = Queue("program.results", true)
        val topicExchange = TopicExchange("program")
        return Declarables(
                tasksQueue,
                resultsQueue,
                topicExchange,
                BindingBuilder.bind(tasksQueue).to(topicExchange).with("program.tasks"),
                BindingBuilder.bind(resultsQueue).to(topicExchange).with("program.results")
        )
    }

}
