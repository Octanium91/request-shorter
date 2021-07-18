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

    String country = "unknown"

    String city = "unknown"

    String device = "unknown"

    boolean mobile = false

    String browser = "unknown"

    String operatingSystem = "unknown"

    String autonomousSystemOrganization = "unknown"

    Integer autonomousSystemNumber = 0

    @Version
    long version

}
