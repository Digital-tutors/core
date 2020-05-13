package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.checker.vo.topic.TopicVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface TopicRepository : MongoRepository<Topic, String> {

    fun findAllByAccessTypeEquals(accessType: AccessType, pageable: Pageable): Page<Topic>
    fun findAllByAccessTypeEqualsOrFollowersContains(AccessType: AccessType, followers: User, pageable: Pageable): Page<Topic>
    fun findAllByFollowersContains(followers: User): List<Topic>
    fun findAllByAuthorIdOrContributorsContains(authorId: User, contributor: User): List<Topic>
    fun findFirstByIdAndAccessTypeOrFollowers(id: String, AccessType: AccessType, followers: User): Topic

}
