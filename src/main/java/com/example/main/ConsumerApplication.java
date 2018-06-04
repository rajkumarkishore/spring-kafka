package com.example.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.example.config.MessageConsumer;

@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		
		ConfigurableApplicationContext context = SpringApplication.run(ConsumerApplication.class, args);

		MessageConsumer consumer = context.getBean(MessageConsumer.class);
		
		try {
			consumer.latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		context.close();
	}


	@Bean
	public MessageConsumer messageConsumer() {
		return new MessageConsumer();
	}
	
}
