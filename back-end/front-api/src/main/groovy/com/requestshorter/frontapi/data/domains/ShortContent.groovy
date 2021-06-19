package com.requestshorter.frontapi.data.domains

import com.requestshorter.frontapi.model.shortContent.ShortContentType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "shortContent")
class ShortContent {

    @Id
    String id

    @CreatedDate
    Date createDate

    @LastModifiedDate
    Date lastModifiedDate

    ShortContentType type

    @Indexed(unique=true)
    String code

    String link

    @Version
    long version

}
