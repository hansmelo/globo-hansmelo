package com.desafio.globo.service;

import com.desafio.globo.domain.message.Notification;
import com.desafio.globo.domain.model.NotificationType;
import com.desafio.globo.domain.model.Status;
import com.desafio.globo.domain.model.Subscription;
import com.desafio.globo.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;

    public Optional<Subscription> save(Notification notification) {
        return repository
                .findById(notification.subscription())
                .map(subscription -> update(notification, subscription))
                .orElseGet(() -> insert(notification));
    }

    private Optional<Subscription> insert(Notification notification) {
        log.info("Inserting {}", notification);

        return Optional.of(
                repository.saveAndFlush(
                        Subscription.builder()
                                .id(notification.subscription())
                                .status(getStatus(notification.type()))
                                .build()
                )
        );
    }

    private Optional<Subscription> update(Notification notification, Subscription subscription) {
        log.info("Updating {}", notification);

        subscription.setStatus(getStatus(notification.type()));
        return Optional.of(repository.saveAndFlush(subscription));
    }

    private Status getStatus(String type) {
        if (NotificationType.SUBSCRIPTION_PURCHASED.name().equals(type) || NotificationType.SUBSCRIPTION_RESTARTED.name().equals(type)) {
            return Status.ACTIVE;
        } else {
            return Status.CANCELED;
        }
    }
}
