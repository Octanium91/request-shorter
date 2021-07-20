package com.requestshorter.frontapi.service

import com.axlabs.ip2asn2cc.Ip2Asn2Cc
import com.axlabs.ip2asn2cc.model.FilterPolicy
import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.model.AsnResponse
import com.maxmind.geoip2.model.CityResponse
import com.maxmind.geoip2.model.CountryResponse
import com.maxmind.geoip2.record.City
import com.maxmind.geoip2.record.Country
import com.requestshorter.frontapi.data.domains.ClientRequest
import com.requestshorter.frontapi.data.domains.ShortContent
import com.requestshorter.frontapi.data.repository.ClientRequestRepository
import com.requestshorter.frontapi.model.clientRequest.ClientRequestIPAPIInfoDto
import com.requestshorter.frontapi.model.clientRequest.ClientRequestUserAgentInfoDto
import com.requestshorter.frontapi.model.geoIP2.GeoIP2Response
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.requestshorter.frontapi.Utils.UAgentInfo

import javax.servlet.http.HttpServletRequest
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Slf4j
@Service
class ClientRequestService {

    @Autowired ClientRequestRepository clientRequestRepository
    @Autowired GeoLite2Service geoLite2Service

    private List<String> listAlpha2CountryCode = [
            "AD","AE","AF","AG","AI","AL","AM","AO","AQ","AR","AS","AT","AU","AW","AX",
            "AZ","BA","BB","BD","BE","BF","BG","BH","BI","BJ","BL","BM","BN","BO","BQ",
            "BR","BS","BT","BV","BW","BY","BZ","CA","CC","CD","CF","CG","CH","CI","CK",
            "CL","CM","CN","CO","CR","CU","CV","CW","CX","CY","CZ","DE","DJ","DK","DM",
            "DO","DZ","EC","EE","EG","EH","ER","ES","ET","FI","FJ","FK","FM","FO","FR",
            "GA","GB","GD","GE","GF","GG","GH","GI","GL","GM","GN","GP","GQ","GR","GS",
            "GT","GU","GW","GY","HK","HM","HN","HR","HT","HU","ID","IE","IL","IM","IN",
            "IO","IQ","IR","IS","IT","JE","JM","JO","JP","KE","KG","KH","KI","KM","KN",
            "KP","KR","KW","KY","KZ","LA","LB","LC","LI","LK","LR","LS","LT","LU","LV",
            "LY","MA","MC","MD","ME","MF","MG","MH","MK","ML","MM","MN","MO","MP","MQ",
            "MR","MS","MT","MU","MV","MW","MX","MY","MZ","NA","NC","NE","NF","NG","NI",
            "NL","NO","NP","NR","NU","NZ","OM","PA","PE","PF","PG","PH","PK","PL","PM",
            "PN","PR","PS","PT","PW","PY","QA","RE","RO","RS","RU","RW","SA","SB","SC",
            "SD","SE","SG","SH","SI","SJ","SK","SL","SM","SN","SO","SR","SS","ST","SV",
            "SX","SY","SZ","TC","TD","TF","TG","TH","TJ","TK","TL","TM","TN","TO","TR",
            "TT","TV","TW","TZ","UA","UG","UM","US","UY","UZ","VA","VC","VE","VG","VI",
            "VN","VU","WF","WS","YE","YT","ZA","ZM","ZW"
    ]

    void create(HttpServletRequest request, ShortContent shortContent) {
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

    int getCountByShortContent(String ShortContentId) {
        clientRequestRepository.countByShortContentId(ShortContentId)
    }

    List<ClientRequest> getAllByShortContent(String ShortContentId) {
        clientRequestRepository.findAllByShortContentId(ShortContentId)
    }

    List<ClientRequest> getAllForLoadStatistic() {
        clientRequestRepository.findAllByLoadStatistic(true)
    }

    ClientRequest save(ClientRequest entity) {
        clientRequestRepository.save(entity)
    }

    //TODO разобраться с этой фигней
    String defineCountryByIPIp2Asn2Cc(String ip) {
        String countryCode = null
        listAlpha2CountryCode.forEach {
            Ip2Asn2Cc ip2Asn2Cc = new Ip2Asn2Cc([it], FilterPolicy.INCLUDE_COUNTRY_CODES)
            ip2Asn2Cc.
            if (ip2Asn2Cc.checkIP(ip)) {
                return it
            }
        }
        countryCode
    }

    String defineCountryByIPProvIPGeo(String ip) {
        log.info("Define country by IP use api.ipgeolocationapi.com")
        String countryAlpha3 = ''
        try {
            URI uri = new URI('https://api.ipgeolocationapi.com/geolocate/' + ip)
//                    ObjectMapper mapper = new ObjectMapper();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .build()
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString())
//            HttpClient.newHttpClient()
//                    .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
//                    .thenApplyAsync({ response ->
            String alpha3 = ""
            if (response.statusCode() == 200) {
                BufferedReader reader = new BufferedReader(new StringReader(response.body()))
                boolean whileState = true
                while (whileState) {
                    alpha3 = reader.readLine()
                    if (alpha3.contains("alpha2")) {
                        if (alpha3.split("\"")[9] == "alpha2") {
                            alpha3 = alpha3.split("\"")[15]
                        } else {
                            alpha3 = alpha3.split("\"")[11]
                        }
                        whileState = false
                    } else {
                        alpha3 = ""
                        whileState = false
                    }
                }
            }
                        countryAlpha3 = alpha3
//                    })
        } catch (e) {
            log.warn("Error define country for ip ${ip}", e)
        }
        countryAlpha3
    }

    ClientRequestIPAPIInfoDto defineCountryByIPProvIPAIP(String ip) {
        log.info("Define country by IP use ipapi.co")
        ClientRequestIPAPIInfoDto ret = new ClientRequestIPAPIInfoDto()
        try {
            URI uri = new URI("https://ipapi.co/${ip}/json/")
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .build()
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString())
            if (response.statusCode() == 200) {
                BufferedReader reader = new BufferedReader(new StringReader(response.body()))
                String alpha3 = ""
                boolean whileState = true
                while (whileState) {
//            response.body().split("\n").collect {
                    alpha3 = reader.readLine()
//                if (alpha3.contains("ip")) {
//                    ret.ip = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("version")) {
//                    ret.version = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("city")) {
//                    ret.city = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("region_code")) {
//                    ret.region_code = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("region")) {
//                    (!ret.region)?ret.region = alpha3.split("\"")[3]:null
//                }
//                if (alpha3.contains("country_name")) {
//                    ret.country_name = alpha3.split("\"")[3]
//                }
                    if (alpha3.contains("country_code")) {
                        ret.country_code = alpha3.split("\"")[3]
                    }
                    if (alpha3.contains("country_code_iso3")) {
                        ret.country_code_iso3 = alpha3.split("\"")[3]
                    }
//                if (alpha3.contains("country_capital")) {
//                    ret.country_capital = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("country_tld")) {
//                    ret.country_tld = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("country")) {
//                    (!ret.country)?ret.country = alpha3.split("\"")[3]:null
//                }
//                if (alpha3.contains("continent_code")) {
//                    ret.continent_code = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("in_eu")) {
//                    ret.in_eu = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("postal")) {
//                    ret.postal = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("latitude")) {
//                    try {
//                        ret.latitude = new Float(alpha3.split("\"")[3])
//                    } catch(e) {}
//                }
//                if (alpha3.contains("longitude")) {
//                    try {
//                        ret.longitude = new Float(alpha3.split("\"")[3])
//                    } catch(e) {}
//                }
//                if (alpha3.contains("timezone")) {
//                    ret.timezone = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("utc_offset")) {
//                    ret.utc_offset = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("country_calling_code")) {
//                    ret.country_calling_code = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("currency_name")) {
//                    ret.currency_name = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("currency")) {
//                    (!ret.currency)?ret.currency = alpha3.split("\"")[3]:null
//                }
//                if (alpha3.contains("languages")) {
//                    ret.languages = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("country_area")) {
//                    try {
//                        ret.country_area = new Float(alpha3.split("\"")[3])
//                    } catch(e) {}
//                }
//                if (alpha3.contains("country_population")) {
//                    try {
//                        ret.country_population = new Float(alpha3.split("\"")[3])
//                    } catch(e) {}
//                }
//                if (alpha3.contains("asn")) {
//                    ret.asn = alpha3.split("\"")[3]
//                }
//                if (alpha3.contains("org")) {
//                    ret.org = alpha3.split("\"")[3]
//                }
                    if (alpha3.contains("}")) {
                        whileState = false
                    }
                }
            }
            ret
        } catch (e) {
            log.warn("Error define country for ip ${ip}", e)
        }
        ret
    }

    GeoIP2Response defineCountryByIPProvGeoIP2(String ip, DatabaseReader ASNReader = null, DatabaseReader CityReader = null) {
        log.info("Define country by IP use GeoIP2")
        GeoIP2Response geoIP2Response = new GeoIP2Response()
        try {
            if (!ASNReader) {
                log.warn("ASN reader is NULL, create reader...")
                ASNReader = new DatabaseReader.Builder(new File(geoLite2Service.getDBPathString("GeoLite2-ASN"))).build()
            }
            if (!CityReader) {
                log.warn("City reader is NULL, create reader...")
                CityReader = new DatabaseReader.Builder(new File(geoLite2Service.getDBPathString("GeoLite2-City"))).build()
            }
            InetAddress ipAddress = InetAddress.getByName(ip)
            AsnResponse asnResponse = ASNReader.asn(ipAddress)
            CityResponse cityResponse = CityReader.city(ipAddress)
            Country country = cityResponse.getCountry()
            City city = cityResponse.getCity()
            geoIP2Response.autonomousSystemNumber = asnResponse.getAutonomousSystemNumber()
            geoIP2Response.autonomousSystemOrganization = asnResponse.getAutonomousSystemOrganization()
            geoIP2Response.countryIso = country.getIsoCode()
            geoIP2Response.cityIso = city.getName()
        } catch(e) {
            log.warn("Error define country for ip ${ip}", e)
        }
        geoIP2Response
    }

    ClientRequestUserAgentInfoDto userAgentInfo(String userAgent) {
        ClientRequestUserAgentInfoDto userAgentDto = new ClientRequestUserAgentInfoDto()
        UAgentInfo uAgentInfo = new UAgentInfo(userAgent, null)

        userAgentDto.mobile = uAgentInfo.isMobilePhone

        if (uAgentInfo.isAndroid) {
            userAgentDto.device = 'android'
        } else if (uAgentInfo.isAndroidPhone) {
            userAgentDto.device = 'android'
        } else if (uAgentInfo.isIphone) {
            userAgentDto.device = 'iPhone'
        } else if (uAgentInfo.detectWindowsMobile()) {
            userAgentDto.device = 'windows mobile'
        } else if (uAgentInfo.detectWindowsPhone()) {
            userAgentDto.device = 'windows phone'
        } else if (uAgentInfo.detectBlackBerry()) {
            userAgentDto.device = 'black berry'
        } else if (uAgentInfo.detectIpad()) {
            userAgentDto.device = 'iPad'
        } else if (uAgentInfo.detectXbox()) {
            userAgentDto.device = 'xBox'
        } else if (uAgentInfo.detectNintendo()) {
            userAgentDto.device = 'nintendo'
        } else {
            userAgentDto.device = 'unknown'
        }


        String user = userAgent.toLowerCase();
        if (userAgent.toLowerCase().indexOf("windows") >= 0 )
        {
            userAgentDto.os = "windows"
        } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
        {
            userAgentDto.os = "mac"
        } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
        {
            userAgentDto.os = "unix"
        } else if(userAgent.toLowerCase().indexOf("android") >= 0)
        {
            userAgentDto.os = "unix"
        } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
        {
            userAgentDto.os = "ios"
        }else{
            userAgentDto.os = "unknown"
        }

        if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0]
            userAgentDto.browserAndVersion=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1]
            userAgentDto.browser="msie"
        } else if (user.contains("safari") && user.contains("version"))
        {
            userAgentDto.browserAndVersion=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1]
            userAgentDto.browser="safari"
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera")) {
                userAgentDto.browserAndVersion = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1]
                userAgentDto.browser = "opera"
            } else if(user.contains("opr")) {
                userAgentDto.browserAndVersion = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera")
                userAgentDto.browser = "opera"
            }
        } else if (user.contains("chrome"))
        {
            userAgentDto.browserAndVersion=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-")
            userAgentDto.browser = "chrome"
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            userAgentDto.browserAndVersion = "Netscape-?";
            userAgentDto.browser = "netscape"

        } else if (user.contains("firefox"))
        {
            userAgentDto.browserAndVersion=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-")
            userAgentDto.browser = "firefox"
        } else if(user.contains("rv"))
        {
            userAgentDto.browserAndVersion="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"))
            userAgentDto.browser = "ie"
        }  else if(user.contains("telegram"))
        {
            userAgentDto.browserAndVersion="telegram"
            userAgentDto.browser = "telegram"
        }  else if(user.contains("postmanruntime"))
        {
            userAgentDto.browserAndVersion="postman"
            userAgentDto.browser = "postman"
        } else
        {
            userAgentDto.browserAndVersion = "unknown"
            userAgentDto.browser = "unknown"
        }

        userAgentDto
    }

}
