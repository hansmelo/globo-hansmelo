package com.desafio.globo.integration;

import com.desafio.globo.domain.message.Notification;
import com.desafio.globo.domain.model.NotificationType;
import com.desafio.globo.domain.model.Status;
import com.desafio.globo.repository.EventHistoryRepository;
import com.desafio.globo.repository.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private EventHistoryRepository eventHistoryRepository;

	static {
		final GenericContainer rabbitMq = new GenericContainer("rabbitmq:3-management").withExposedPorts(5672);
		rabbitMq.start();

		final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
				.withDatabaseName("integration-tests-db")
				.withUsername("sa")
				.withPassword("sa");

		postgreSQLContainer.start();


		// Pass the properties directly to the app. Do not use properties file.
		System.setProperty("spring.rabbitmq.host", rabbitMq.getContainerIpAddress());
		System.setProperty("spring.rabbitmq.port", rabbitMq.getMappedPort(5672).toString());
		System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
		System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
		System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
	}

	private Faker faker;
	private Notification notification;
	private Notification restartedNotification;
	private Notification canceledNotification;


	@BeforeEach
	void setUp() {
		faker = new Faker();
		notification = new Notification(NotificationType.SUBSCRIPTION_PURCHASED.name(), faker.code().gtin8());
		canceledNotification = new Notification(NotificationType.SUBSCRIPTION_CANCELED.name(), notification.subscription());
		restartedNotification = new Notification(NotificationType.SUBSCRIPTION_RESTARTED.name(), faker.code().gtin8());
		subscriptionRepository.deleteAll();
		eventHistoryRepository.deleteAll();
	}

	@Test
	public void postOneNotificationShouldReturnAcceptedAndSaveOneSubscriptionAndOneEventHistory() throws Exception {
		var urlNotification = "http://localhost:" + port + "/notifications";
		ResponseEntity<String> response = this.restTemplate.postForEntity(urlNotification, notification, String.class);


		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(objectMapper.readValue(response.getBody(), Notification.class)).isEqualTo(notification);

		await().until(() -> subscriptionRepository.findAll().size() == 1);
		assertThat(subscriptionRepository.findAll().get(0).getStatus()).isEqualTo(Status.ACTIVE);

		await().until(() -> eventHistoryRepository.findAll().size() == 1);
		assertThat(eventHistoryRepository.findAll().get(0).getType()).isEqualTo(NotificationType.SUBSCRIPTION_PURCHASED);
	}

	@Test
	public void postTwoNotificationFromTheSameSubscritionShouldReturnAcceptedAndSaveOneSubscriptionAndTwoEventHistory() throws Exception {
		var urlNotification = "http://localhost:" + port + "/notifications";
		ResponseEntity<String> response = this.restTemplate.postForEntity(urlNotification, notification, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(objectMapper.readValue(response.getBody(), Notification.class)).isEqualTo(notification);

		ResponseEntity<String> canceledResponse = this.restTemplate.postForEntity(urlNotification, canceledNotification, String.class);


		assertThat(canceledResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(objectMapper.readValue(canceledResponse.getBody(), Notification.class)).isEqualTo(canceledNotification);

		await().until(() -> eventHistoryRepository.findAll().size() == 2);
		assertThat(eventHistoryRepository.findAll().get(0).getType()).isEqualTo(NotificationType.SUBSCRIPTION_PURCHASED);
		assertThat(eventHistoryRepository.findAll().get(1).getType()).isEqualTo(NotificationType.SUBSCRIPTION_CANCELED);

		await().until(() -> subscriptionRepository.findAll().size() == 1);
		assertThat(subscriptionRepository.findAll().get(0).getStatus()).isEqualTo(Status.CANCELED);

	}

	@Test
	public void postTwoNotificationFromTheDifferentSubscritions_ShouldReturnAccepted_AndSaveTwoSubscription_AndTwoEventHistory() throws Exception {
		var urlNotification = "http://localhost:" + port + "/notifications";
		ResponseEntity<String> response = this.restTemplate.postForEntity(urlNotification, notification, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(objectMapper.readValue(response.getBody(), Notification.class)).isEqualTo(notification);

		ResponseEntity<String> restartedResponse = this.restTemplate.postForEntity(urlNotification, restartedNotification, String.class);

		assertThat(restartedResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(objectMapper.readValue(restartedResponse.getBody(), Notification.class)).isEqualTo(restartedNotification);

		await().until(() -> eventHistoryRepository.findAll().size() == 2);
		assertThat(eventHistoryRepository.findAll().get(0).getType()).isEqualTo(NotificationType.SUBSCRIPTION_PURCHASED);
		assertThat(eventHistoryRepository.findAll().get(1).getType()).isEqualTo(NotificationType.SUBSCRIPTION_RESTARTED);

		await().until(() -> subscriptionRepository.findAll().size() == 2);
		assertThat(subscriptionRepository.findAll().get(0).getStatus()).isEqualTo(Status.ACTIVE);
		assertThat(subscriptionRepository.findAll().get(1).getStatus()).isEqualTo(Status.ACTIVE);

	}

	@Test
	public void postWrongNotificationShouldReturnBadRequest() throws Exception {
		var urlNotification = "http://localhost:" + port + "/notifications";
		ResponseEntity<String> response = this.restTemplate.postForEntity(urlNotification, new Notification(null, null), String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}
}
