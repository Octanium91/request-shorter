package com.requestshorter.entityapi.service

import javax.servlet.http.HttpServletRequest

class RequestServletService {

    static String getIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For")
        if (!ipAddress) {
            ipAddress = request.getHeader("X-Real-IP")
        }
        if (!ipAddress) {
            ipAddress = request.getRemoteAddr()
        }
        if (ipAddress) {
            try {
                ipAddress = ipAddress.split()[0].replace(",","")
            } catch(e) {

            }
        }
        ipAddress
    }

    static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader( "X-User-Agent")
        if (!userAgent) {
            userAgent = request.getHeader( "User-Agent")
        }
        userAgent
    }

}
