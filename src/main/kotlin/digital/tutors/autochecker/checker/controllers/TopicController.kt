package digital.tutors.autochecker.checker.controllers

import digital.tutors.autochecker.checker.services.TopicService
import digital.tutors.autochecker.checker.vo.topic.TopicCreateRq
import digital.tutors.autochecker.checker.vo.topic.TopicUpdateRq
import digital.tutors.autochecker.checker.vo.topic.TopicVO
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

    @GetMapping("/topics")
    fun getTopics(@RequestParam page: Int): ResponseEntity<Page<TopicVO>> = processServiceExceptions {
        /*
        TODO: Получение тем с PUBLIС ключом
         */

        try {
            val pageRequest = PageRequest.of(page, 10)
            ResponseEntity.ok(topicService.getTopics(pageRequest))
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

    @DeleteMapping("/topic/{id}")
    fun deleteAdvantage(@PathVariable id: String): ResponseEntity<*> = processServiceExceptions {
        ResponseEntity.ok(topicService.delete(id))
    }

}
