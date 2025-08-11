package com.votingSystem.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
		basePackages = {
				"com.votingSystem.voting.config",
				"com.votingSystem.voting.controller",
				"com.votingSystem.voting.dto",
				"com.votingSystem.voting.entity",
				"com.votingSystem.voting.exception",
				"com.votingSystem.voting.repository",
				"com.votingSystem.voting.security",
				"com.votingSystem.voting.service"
		},
		includeFilters = @ComponentScan.Filter(
				type = FilterType.ANNOTATION,
				classes = {org.springframework.stereotype.Service.class,
						org.springframework.stereotype.Controller.class,
						org.springframework.stereotype.Repository.class}
		),
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.REGEX,
				pattern = "com\\.votingSystem\\.voting\\.experimental.*"
		)
)
public class VotingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingApplication.class, args);
	}

}
