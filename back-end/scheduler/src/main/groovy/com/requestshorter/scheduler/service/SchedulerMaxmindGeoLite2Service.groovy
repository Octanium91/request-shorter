package com.requestshorter.scheduler.service


import com.requestshorter.frontapi.service.GeoLite2Service
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Slf4j
@Service
class SchedulerMaxmindGeoLite2Service {

    @Autowired private GeoLite2Service geoLite2Service

    @Scheduled(fixedDelay = 43200000L, initialDelay = 500L)
    private void defaultScheduler() {
        String sha256ASN = geoLite2Service.getSha256("GeoLite2-ASN")
        if (sha256ASN) {
            if (geoLite2Service.sha256IsExist("GeoLite2-ASN", sha256ASN)) {
                log.info("Maxmind GeoLite2: Database GeoLite2-ASN not need update!")
            } else {
                geoLite2Service.updateDb("GeoLite2-ASN", sha256ASN)
            }
        }
        String sha256City = geoLite2Service.getSha256("GeoLite2-City")
        if (sha256City) {
            if (geoLite2Service.sha256IsExist("GeoLite2-City", sha256City)) {
                log.info("Maxmind GeoLite2: Database GeoLite2-City not need update!")
            } else {
                geoLite2Service.updateDb("GeoLite2-City", sha256City)
            }
        }
//        String sha256Country = geoLite2Service.getSha256("GeoLite2-Country")
//        if (sha256Country) {
//            if (geoLite2Service.sha256IsExist("GeoLite2-Country", sha256Country)) {
//                log.info("Maxmind GeoLite2: Database GeoLite2-Country not need update!")
//            } else {
//                geoLite2Service.updateDb("GeoLite2-Country", sha256Country)
//            }
//        }
    }

}
