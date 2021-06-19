package com.requestshorter.frontapi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@EnableMongoAuditing
@SpringBootApplication(scanBasePackageClasses = [FrontApiApplication.class])
class FrontApiApplication {

	static void main(String[] args) {
		SpringApplication.run(FrontApiApplication, args)
	}

}
