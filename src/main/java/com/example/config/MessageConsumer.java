package com.example.config;

import java.util.concurrent.CountDownLatch;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public class MessageConsumer {
	
	public CountDownLatch latch = new CountDownLatch(3);

	@KafkaListener(topics = "${message.topic.name}", groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
	public void listenGroupFoo(String message) {
		System.out.println("Received message in group 'foo': " + message);
		latch.countDown();
	}

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "headersKafkaListenerContainerFactory")
	public void listenWithHeaders(@Payload String message, @Header(KafkaHeaders.PARTITION_ID) int partition) {
		System.out.println("Received Messasge: " + message + " from partition: " + partition);
		latch.countDown();
	}

}
