//package digital.tutors.autochecker.rabbitmq
//
//import org.springframework.amqp.rabbit.annotation.RabbitHandler
//import org.springframework.amqp.rabbit.annotation.RabbitListener
//import org.springframework.stereotype.Service
//import java.util.concurrent.CountDownLatch
//
//@Service
//@RabbitListener(queues = ["autochecker"])
//class Consumer {
//
//    private val latch = CountDownLatch(1)
//
//    @RabbitHandler
//    fun receiveMessage(message: String) {
//        println("Received <$message>")
//        latch.countDown()
//    }
//
//}