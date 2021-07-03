package com.requestshorter.frontapi.controller

import com.requestshorter.frontapi.data.domains.ShortContent
import com.requestshorter.frontapi.model.shortContent.ShortContentType
import com.requestshorter.frontapi.service.ClientRequestService
import com.requestshorter.frontapi.service.ShortContentService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.view.RedirectView


import javax.servlet.http.HttpServletRequest

@Slf4j
@RestController
class OpenAPIController {

    @Value('${baseHost}')
    private final String baseHost = ''

    @Autowired private ShortContentService shortContentService
    @Autowired private ClientRequestService clientRequestService

    @GetMapping("/api/open/create/link")
    String doCreate(@RequestParam String link) {
        shortContentService.createLink(link)
    }

    @GetMapping("/api/open/execute")
    RedirectView doExecute(HttpServletRequest request, @RequestParam String shortCode) {
        ShortContent entity = shortContentService.getByCode(shortCode)
        if (entity) {
            if (entity.type == ShortContentType.LINK) {
                clientRequestService.create(request, entity)
                new RedirectView(entity.link)
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Open API, short link ${shortCode} not found!"
            );
        }
    }

}
