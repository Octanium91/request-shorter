package com.requestshorter.frontapi.controller

import com.requestshorter.frontapi.data.domains.ShortContent
import com.requestshorter.frontapi.model.shortContent.ShortContentCreateDto
import com.requestshorter.frontapi.service.ClientRequestService
import com.requestshorter.frontapi.service.ShortContentService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@Slf4j
@RestController
class ShortContentController {

    @Autowired private ShortContentService shortContentService
    @Autowired private ClientRequestService clientRequestService

    @PutMapping("/api/shortContent/create")
    String createShortContent(@RequestBody ShortContentCreateDto shortContentCreateDto) {
        shortContentService.createLink(shortContentCreateDto.link)
    }

    @GetMapping("/api/shortContent/statistic/count")
    int getStatisticCount(@RequestParam String code) {
        ShortContent shortContent = shortContentService.getByCode(code)
        if (shortContent) {
            clientRequestService.getCountByShortContent(shortContent.id)
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ShortContent with code ${code} not found!")
        }
    }

}
