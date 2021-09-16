package com.desafio.globo.service;

import com.desafio.globo.domain.message.Notification;
import com.desafio.globo.domain.model.EventHistory;
import com.desafio.globo.domain.model.NotificationType;
import com.desafio.globo.domain.model.Status;
import com.desafio.globo.domain.model.Subscription;
import com.desafio.globo.repository.EventHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class EventHistoryService {

    private final EventHistoryRepository repository;

    public EventHistory save(Notification notification, Subscription subscription) {
        EventHistory eventHistory = EventHistory.builder()
                .subscription(subscription)
                .type(NotificationType.valueOf(notification.type()))
                .build();

        EventHistory eventHistorySaved = repository.saveAndFlush(eventHistory);
        log.info("Saved {}", eventHistorySaved);

        return eventHistorySaved;
    }
}
