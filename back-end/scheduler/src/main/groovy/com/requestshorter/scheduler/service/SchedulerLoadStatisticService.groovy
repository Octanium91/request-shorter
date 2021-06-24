package com.requestshorter.scheduler.service

import com.requestshorter.frontapi.model.clientRequest.ClientRequestIPAPIInfoDto
import com.requestshorter.frontapi.model.clientRequest.ClientRequestUserAgentInfoDto
import com.requestshorter.frontapi.model.geoIP2.GeoIP2Response
import com.requestshorter.frontapi.service.ClientRequestService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Slf4j
@Service
class SchedulerLoadStatisticService {

    @Autowired private ClientRequestService clientRequestService

    @Scheduled(fixedDelay = 1000L)
    private void defaultScheduler() {
        clientRequestService.getAllForLoadStatistic().forEach({
            log.info("Load statistic for ${it.id}...")
            String countryCode = ""
            String cityName = ""
            ClientRequestUserAgentInfoDto userAgentInfo = new ClientRequestUserAgentInfoDto()
            if (it.ipAddress) {
                GeoIP2Response geoIP2Response = clientRequestService.defineCountryByIPProvGeoIP2(it.ipAddress)
                countryCode = geoIP2Response.countryIso
                cityName = geoIP2Response.cityIso
                if (!countryCode) {
                    ClientRequestIPAPIInfoDto IPAPIInfo = clientRequestService.defineCountryByIPProvIPAIP(it.ipAddress)
                    countryCode = IPAPIInfo.country_code
                    if (!countryCode) {
                        countryCode = clientRequestService.defineCountryByIPProvIPGeo(it.ipAddress)
                    }
                }
            }
            if (it.userAgent) {
                userAgentInfo = clientRequestService.userAgentInfo(it.userAgent)
            }
            if (countryCode) {
                it.country = countryCode
            }
            if (cityName) {
                it.city = cityName
            }
            it.mobile = userAgentInfo.mobile
            it.operatingSystem = userAgentInfo.os
            it.browser = userAgentInfo.browser
            it.loadStatistic = false
            if (countryCode) {
                clientRequestService.save(it)
            }
        })
    }

}
