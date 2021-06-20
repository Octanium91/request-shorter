package com.requestshorter.scheduler

import com.requestshorter.frontapi.FrontApiApplication
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication(scanBasePackageClasses = [FrontApiApplication.class, SchedulerApplication.class])
class SchedulerApplication {

	static void main(String[] args) {
		SpringApplication.run(SchedulerApplication, args)
	}

}
