package com.requestshorter.frontapi.data.repository

import com.requestshorter.frontapi.data.domains.ShortContent
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ShortContentRepository extends MongoRepository<ShortContent, String> {

    Optional<ShortContent> findByCode(String name)

    Long countByCreateDateGreaterThan(Date date)

}