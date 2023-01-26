package com.requestshorter.frontapi.data.domains

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "clientRequest")
class ClientRequest {

    @Id
    String id

    @CreatedDate
    @Indexed(background = true, direction = IndexDirection.DESCENDING)
    Date createDate

    @LastModifiedDate
    @Indexed(background = true, direction = IndexDirection.DESCENDING)
    Date lastModifiedDate

    @Indexed(background = true)
    String shortContentId

    String ipAddress

    String userAgent

    @Indexed(background = true)
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
