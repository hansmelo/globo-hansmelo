package com.desafio.globo.service;

import com.desafio.globo.domain.message.Notification;
import com.desafio.globo.domain.model.EventHistory;
import com.desafio.globo.domain.model.NotificationType;
import com.desafio.globo.domain.model.Status;
import com.desafio.globo.domain.model.Subscription;
import com.desafio.globo.repository.EventHistoryRepository;
import com.desafio.globo.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Optional;

import static com.desafio.globo.configuration.RabbitMQConfig.QUEUE_NAME;

@Slf4j
@AllArgsConstructor
@Service
public class NotificationListener {

    private SubscriptionService subscriptionService;
    private EventHistoryService eventHistoryService;

    @Transactional
    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(Notification notification) {
        log.info("Received {}", notification);
        subscriptionService
                .save(notification)
                .ifPresent(subscription -> eventHistoryService.save(notification, subscription));
    }
}
