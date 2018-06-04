package com.baeldung.spring.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class DemoProducer {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(DemoProducer.class, args);

		MessageProducer producer = context.getBean(MessageProducer.class);
		
		//producer.sendMessage("Hello, World!");

		/*for (int i = 0; i < 8; i++) {
			producer.sendMessageToPartion("Hello To Partioned Topic!", i + "");
		}*/

	    //producer.sendMessageToFiltered("Hello Baeldung!");
		
		producer.sendGreetingMessage(new Greeting("Greetings", "World!"));
		
		context.close();
	}

	@Bean
	public MessageProducer messageProducer() {
		return new MessageProducer();
	}

	public static class MessageProducer {

		@Autowired
		private KafkaTemplate<String, String> kafkaTemplate;

		@Autowired
		private KafkaTemplate<String, Greeting> greetingKafkaTemplate;

		@Value(value = "${message.topic.name}")
		private String topicName;

		@Value(value = "${partitioned.topic.name}")
		private String partionedTopicName;

		@Value(value = "${filtered.topic.name}")
		private String filteredTopicName;

		@Value(value = "${greeting.topic.name}")
		private String greetingTopicName;

		public void sendMessage(String message) {
			kafkaTemplate.send(topicName, message);
		}

		public void sendMessageToPartion(String message, String partition) {
			kafkaTemplate.send(partionedTopicName, partition, message);

		}

		public void sendMessageToFiltered(String message) {
			kafkaTemplate.send(filteredTopicName, message);
		}

		public void sendGreetingMessage(Greeting greeting) {
			greetingKafkaTemplate.send(greetingTopicName, greeting);
		}
	}

}