package com.requestshorter.frontapi.controller

import com.requestshorter.frontapi.data.domains.ShortContent
import com.requestshorter.frontapi.model.statistic.StatisticData
import com.requestshorter.frontapi.model.statistic.StatisticResponseDto
import com.requestshorter.frontapi.service.ClientRequestService
import com.requestshorter.frontapi.service.ShortContentService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class StatisticController {

    @Autowired ShortContentService shortContentService
    @Autowired ClientRequestService clientRequestService

    @GetMapping("/api/click/statistic")
    StatisticResponseDto getStatistic(@RequestParam String code) {

        ShortContent shortContent = shortContentService.getByCode(code)
        if (shortContent) {
            Map<String, Integer> countryMap = [:]
            Map<String, Integer> osMap = [:]
            clientRequestService.getAllByShortContent(shortContent.id).forEach {
                if (!it.loadStatistic) {
                    if (countryMap.get(it.country)) {
                        countryMap.put(it.country, countryMap.get(it.country) + 1)
                    } else {
                        countryMap.put(it.country, 1)
                    }
                    if (osMap.get(it.operatingSystem)) {
                        osMap.put(it.operatingSystem, osMap.get(it.operatingSystem) + 1)
                    } else {
                        osMap.put(it.operatingSystem, 1)
                    }
                }
            }

            List<StatisticData> countryList = []
            countryMap.each { countryList.add(new StatisticData(value: it.key, count: it.value)) }
            List<StatisticData> osList = []
            osMap.each { osList.add(new StatisticData(value: it.key, count: it.value)) }

            new StatisticResponseDto(
                    country: countryList,
                    os: osList
            )
        } else {
            log.warn("Short content ${code} not found!")
        }

    }

}
