package com.requestshorter.entityapi

import com.requestshorter.frontapi.FrontApiApplication
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackageClasses = [FrontApiApplication.class, EntityApiApplication.class])
class EntityApiApplication {

	static void main(String[] args) {
		SpringApplication.run(EntityApiApplication, args)
	}

}
