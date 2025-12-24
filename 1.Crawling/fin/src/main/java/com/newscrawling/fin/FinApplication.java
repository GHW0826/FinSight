package com.newscrawling.fin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = "com.newscrawling")
@EnableScheduling
public class FinApplication {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(FinApplication.class, args);
		Arrays.stream(ctx.getBeanDefinitionNames())
				.filter(name -> name.contains("Controller"))
				.forEach(System.out::println);
	}
}
