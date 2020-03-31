package digital.tutors.autochecker.checker.repositories

import digital.tutors.autochecker.checker.entities.Task
import org.springframework.data.mongodb.repository.MongoRepository

interface TaskRepository: MongoRepository<Task, String>