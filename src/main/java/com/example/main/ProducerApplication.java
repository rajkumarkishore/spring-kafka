package com.example.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.example.config.MessageProducer;

@SpringBootApplication
public class ProducerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ProducerApplication.class, args);

		MessageProducer producer = context.getBean(MessageProducer.class);
		producer.sendMessage("Hello! This is a message from Spring Kafka project.");

		context.close();
	}

	@Bean
	public MessageProducer messageProducer() {
		return new MessageProducer();
	}

}
