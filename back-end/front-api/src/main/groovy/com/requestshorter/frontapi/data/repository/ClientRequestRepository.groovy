package com.requestshorter.frontapi.data.repository

import com.requestshorter.frontapi.data.domains.ClientRequest
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRequestRepository extends MongoRepository<ClientRequest, String> {

    List<ClientRequest> findAllByShortContentId(String shortContentId)

    List<ClientRequest> findAllByLoadStatistic(boolean loadStatistic)

    Long countByShortContentId(String shortContentId)

    Long countByCreateDateGreaterThan(Date date)

}