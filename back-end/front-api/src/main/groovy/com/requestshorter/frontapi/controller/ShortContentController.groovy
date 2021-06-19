package com.requestshorter.frontapi.controller

import com.requestshorter.frontapi.model.shortContent.ShortContentCreateDto
import com.requestshorter.frontapi.service.ShortContentService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class ShortContentController {

    @Autowired private ShortContentService shortContentService

    @PutMapping("/api/shortContent/create")
    String createShortContent(@RequestBody ShortContentCreateDto shortContentCreateDto) {
        shortContentService.createLink(shortContentCreateDto.link)
    }

}
