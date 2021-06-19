package com.requestshorter.entityapi.service

import com.requestshorter.frontapi.data.domains.ShortContent
import com.requestshorter.frontapi.service.ShortContentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EntityService {

    @Autowired ShortContentService shortContentService

    ShortContent getEntity(String code) {
        shortContentService.getByCode(code)
    }

}
