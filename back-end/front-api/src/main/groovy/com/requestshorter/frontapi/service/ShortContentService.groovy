package com.requestshorter.frontapi.service

import com.requestshorter.frontapi.data.domains.ShortContent
import com.requestshorter.frontapi.data.repository.ShortContentRepository
import com.requestshorter.frontapi.model.shortContent.ShortContentType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Slf4j
@Service
class ShortContentService {

    @Autowired private ShortContentRepository shortContentRepository

    static String generateCode(int symbols) {
        String alphabet = (('A'..'N') + ('P'..'Z') + ('a'..'k') + ('m'..'z') + ('2'..'9')).join()
        new Random().with {
            (1..symbols).collect { alphabet[nextInt(alphabet.length())] }.join()
        }
    }

    String getNewCode() {
        int codeLength = 2
        Page page = shortContentRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC,"createDate")))
        if (page.content.size() == 1) {
            codeLength = page.content[0].code.length()
        }
        int loopCount = 0
        boolean codeGen = true
        String code = ''

        while (codeGen) {
            if (loopCount > 10000) {
                loopCount = 0
                codeLength = codeLength+1
            }
            loopCount = loopCount+1
            code = generateCode(codeLength)
            codeGen = (shortContentRepository.findByCode(code).orElse(null))
        }
        log.info("Short content, code ${code} generated!")
        code
    }

    String createLink(String link) {
        boolean write = true
        ShortContent shortContent = null
        while (write) {
            try {
                shortContent = shortContentRepository.save(new ShortContent(
                        type: ShortContentType.LINK,
                        link: link,
                        code: getNewCode()
                ))
                write = false
            } catch(e) {
                log.warn("Short content is not created!")
            }
        }
        log.info("Short content, created! code: ${shortContent.code}")
        shortContent.code
    }

    ShortContent getByCode(String code) {
        shortContentRepository.findByCode(code).orElse(null)
    }

}
