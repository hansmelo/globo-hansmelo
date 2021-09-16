package com.desafio.globo.service;

import com.desafio.globo.domain.message.Notification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.desafio.globo.configuration.RabbitMQConfig.QUEUE_NAME;

@Slf4j
@AllArgsConstructor
@Service
public class NotificationSender {

    private final RabbitTemplate rabbitTemplate;

    public Boolean send(Notification notification) {
        log.info("Sending {}", notification);
        rabbitTemplate.convertAndSend(QUEUE_NAME, notification);
        return true;
    }
}
