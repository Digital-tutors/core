package digital.tutors.autochecker.reviewer.controllers

import digital.tutors.autochecker.reviewer.services.PeerTaskResultsService
import digital.tutors.autochecker.core.controller.BaseController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class PeerResultsController: BaseController() {

    @Autowired
    lateinit var peerTaskResultsService: PeerTaskResultsService



}