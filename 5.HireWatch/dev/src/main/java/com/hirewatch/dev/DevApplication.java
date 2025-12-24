package com.hirewatch.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = "com.hirewatch")
@EnableScheduling
public class DevApplication {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(DevApplication.class, args);
		Arrays.stream(ctx.getBeanDefinitionNames())
				.filter(name -> name.contains("Controller"))
				.forEach(System.out::println);
	}

}
