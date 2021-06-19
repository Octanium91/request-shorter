package com.requestshorter.frontapi.service

import com.requestshorter.frontapi.data.domains.ClientRequest
import com.requestshorter.frontapi.data.domains.ShortContent
import com.requestshorter.frontapi.data.repository.ClientRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.servlet.http.HttpServletRequest

@Service
class ClientRequestService {

    @Autowired ClientRequestRepository clientRequestRepository

    void create(HttpServletRequest request, ShortContent shortContent) {
        String ipAddress = request.getHeader("X-Forwarded-For")
        if (!ipAddress) {
            ipAddress = request.getHeader("X-Real-IP")
        }
        if (!ipAddress) {
            ipAddress = request.getRemoteAddr()
        }
        String userAgent = request.getHeader( "X-User-Agent")
        if (!userAgent) {
            userAgent = request.getHeader( "User-Agent")
        }
        clientRequestRepository.save(new ClientRequest(
                shortContentId: shortContent.id,
                ipAddress: ipAddress,
                userAgent: userAgent
        ))
    }

}
