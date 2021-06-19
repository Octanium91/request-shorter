package com.requestshorter.frontapi.data.domains

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "clientRequest")
class ClientRequest {

    @Id
    String id

    @CreatedDate
    Date createDate

    @LastModifiedDate
    Date lastModifiedDate

    String shortContentId

    String ipAddress

    String userAgent

    boolean loadStatistic = true

    String country = ""

    String device = ""

    String operatingSystem = ""

    @Version
    long version

}
