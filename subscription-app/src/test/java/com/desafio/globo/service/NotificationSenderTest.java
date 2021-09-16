package com.desafio.globo.service;

import com.desafio.globo.domain.message.Notification;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static com.desafio.globo.configuration.RabbitMQConfig.QUEUE_NAME;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class NotificationSenderTest {

    private Faker faker;
    private Notification notification;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        notification = new Notification(faker.job().field(), faker.code().gtin8());
    }

    @Test
    public void sendNotificationShouldReturnTrue() {
        var rabbitTemplate = mock(RabbitTemplate.class);
        doNothing().when(rabbitTemplate).convertAndSend(QUEUE_NAME, isA(Notification.class));

        var notificationSender = new NotificationSender(rabbitTemplate);

        assertTrue(notificationSender.send(notification));
        verify(rabbitTemplate, times(1)).convertAndSend(QUEUE_NAME, notification);

    }
}
