package com.baeldung.spring.kafka;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@SpringBootApplication
public class DemoConsumer {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(DemoConsumer.class, args);

		MessageListener listener = context.getBean(MessageListener.class);

		/*listener.latch.await(10, TimeUnit.SECONDS);*/
		//listener.latch.await();
		//listener.barLatch.await();
		//listener.partitionLatch.await();

		//listener.filterLatch.await();

		listener.greetingLatch.await();

		// context.close();
	}

	@Bean
	public MessageListener messageListener() {
		return new MessageListener();
	}

	public static class MessageListener {

		private CountDownLatch latch = new CountDownLatch(3);
		
		private CountDownLatch barLatch = new CountDownLatch(3);
		
		private CountDownLatch partitionLatch = new CountDownLatch(2);

		private CountDownLatch filterLatch = new CountDownLatch(2);

		private CountDownLatch greetingLatch = new CountDownLatch(1);

		//@KafkaListener(topics = "${message.topic.name}", groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
		public void listenGroupFoo(String message) {
			System.out.println("Received Messasge in group 'foo': " + message);
			latch.countDown();
			
		}

		//@KafkaListener(topics = "${message.topic.name}", groupId = "bar", containerFactory = "barKafkaListenerContainerFactory")
		public void listenGroupBar(String message) {
			System.out.println("Received Messasge in group 'bar': " + message);
			/*latch.countDown();*/
			barLatch.countDown();
		}
		
		//@KafkaListener(topics = "${message.topic.name}", containerFactory = "headersKafkaListenerContainerFactory")
		public void listenWithHeaders(@Payload String message,
				@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
			System.out.println("Received Messasge: " + message + " from partition: " + partition);
			latch.countDown();
		}
		
		//@KafkaListener(topicPartitions = @TopicPartition(topic = "${partitioned.topic.name}", partitions = { "0", "3" }))
		public void listenToParition(@Payload String message,
				@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
			System.out.println("Received Message: " + message + " from partition: " + partition);
			this.partitionLatch.countDown();
		}
		
		//@KafkaListener(topics = "${filtered.topic.name}", containerFactory = "filterKafkaListenerContainerFactory")
		public void listenWithFilter(String message) {
			System.out.println("Recieved Message in filtered listener: " + message);
			this.filterLatch.countDown();
		}
		
		@KafkaListener(topics = "${greeting.topic.name}", containerFactory = "greetingKafkaListenerContainerFactory")
		public void greetingListener(Greeting greeting) {
			System.out.println("Recieved greeting message: " + greeting);
			this.greetingLatch.countDown();
		}

	}

}