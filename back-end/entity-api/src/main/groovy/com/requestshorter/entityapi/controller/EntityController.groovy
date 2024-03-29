package com.requestshorter.entityapi.controller

import com.requestshorter.entityapi.service.EntityService
import com.requestshorter.entityapi.service.RequestServletService
import com.requestshorter.frontapi.data.domains.ShortContent
import com.requestshorter.frontapi.model.shortContent.ShortContentType
import com.requestshorter.frontapi.service.ClientRequestService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

import javax.servlet.http.HttpServletRequest

@Slf4j
@RestController
class EntityController {

    @Value('${baseHost}')
    private final String baseHost = ''

    @Autowired EntityService entityService
    @Autowired ClientRequestService clientRequestService

    @GetMapping("/e/{shortCode}")
    RedirectView executeEntity(HttpServletRequest request, @PathVariable String shortCode) {
        ShortContent entity = entityService.getEntity(shortCode)
        if (entity) {
            if (entity.type == ShortContentType.LINK) {
                clientRequestService.createAsync(RequestServletService.getIP(request), RequestServletService.getUserAgent(request), entity)
                new RedirectView(entity.link)
            }
        } else {
            new RedirectView("${baseHost}/404/${shortCode}");
        }
    }

    @GetMapping("/code/error/{shortCode}")
    String entityNotFound(@PathVariable String shortCode) {
        "${shortCode} not found!"
    }
}
