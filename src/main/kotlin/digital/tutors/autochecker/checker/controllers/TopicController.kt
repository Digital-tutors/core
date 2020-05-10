package digital.tutors.autochecker.checker.controllers

import digital.tutors.autochecker.auth.services.UserService
import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.checker.services.TopicService
import digital.tutors.autochecker.checker.vo.topic.TopicCreateRq
import digital.tutors.autochecker.checker.vo.topic.TopicUpdateRq
import digital.tutors.autochecker.checker.vo.topic.TopicVO
import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.controller.BaseController
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping
class TopicController : BaseController() {

    /*
     TODO: Проверка на id пользователей
     TODO: Добавление подписчиков и контрибьюторов отдельным роутом
     */

    @Autowired
    lateinit var topicService: TopicService

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/user/{user}/topics")
    fun getSubscribedTopics(@PathVariable user: String): ResponseEntity<List<TopicVO>> = processServiceExceptions {
        try {
            ResponseEntity.ok(topicService.getSubscribedTopics(user))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Subscribed topics Not Found", ex)
        }
    }

    @GetMapping("/topics")
    fun getTopics(@RequestParam page: Int): ResponseEntity<Page<TopicVO>> = processServiceExceptions {
        /*
        TODO: Получение тем с PUBLIС и PRIVATE ключом
         */

        try {
            val id = authorizationService.currentUserIdOrDie()
            val pageRequest = PageRequest.of(page, 10)
            ResponseEntity.ok(topicService.getAllTopicsById(pageRequest, id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Topics Not Found", ex)
        }
    }

    @GetMapping("/topic/{id}")
    fun getTopicById(@PathVariable id: String): ResponseEntity<TopicVO> = processServiceExceptions {
        try {
            ResponseEntity.ok(topicService.getTopicByIdOrThrow(id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Topic Not Found", ex)
        }
    }

    @PostMapping("/topic")
    fun createTopic(@RequestBody topicCreateRq: TopicCreateRq): ResponseEntity<TopicVO> = processServiceExceptions {
        ResponseEntity.ok(topicService.createTopic(topicCreateRq))
    }

    @PutMapping("/topic/{id}")
    fun updateTopic(
            @PathVariable id: String,
            @RequestBody topicUpdateRq: TopicUpdateRq
    ): ResponseEntity<TopicVO> = processServiceExceptions {
        ResponseEntity.ok(topicService.updateTopic(id, topicUpdateRq))
    }

    @PostMapping("/topic/{id}/user/{user}")
    fun subscribeTopic(@PathVariable id: String, @PathVariable user: String): ResponseEntity<*> = processServiceExceptions {
        // TODO: Возможно стоит брать айди из токена, но сделаем потом
        ResponseEntity.ok(topicService.subscribeTopic(id, user))
    }

    @DeleteMapping("/topic/{id}/user")
    fun unSubscribeTopic(@PathVariable id: String): ResponseEntity<*> = processServiceExceptions {
        ResponseEntity.ok(topicService.unSubscribeTopic(id, authorizationService.currentUserIdOrDie()))
    }

    @DeleteMapping("/topic/{id}")
    fun deleteAdvantage(@PathVariable id: String): ResponseEntity<*> = processServiceExceptions {
        ResponseEntity.ok(topicService.delete(id))
    }

}
