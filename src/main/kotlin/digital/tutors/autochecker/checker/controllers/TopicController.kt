package digital.tutors.autochecker.checker.controllers

import digital.tutors.autochecker.auth.vo.UserCreateRq
import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.checker.services.TopicService
import digital.tutors.autochecker.checker.vo.TopicVO
import digital.tutors.autochecker.core.controller.BaseController
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping
class TopicController : BaseController() {

    @Autowired
    lateinit var topicService: TopicService

    @GetMapping("/topics")
    fun getTopics(@RequestParam page: Int): ResponseEntity<Page<TopicVO>> = processServiceExceptions {
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
    fun createTopic(@RequestBody topic: Topic): ResponseEntity<TopicVO> = processServiceExceptions {
        ResponseEntity.ok(topicService.createTopic(topic))
    }

}