package com.desafio.globo.controller;

import com.desafio.globo.domain.message.Notification;
import com.desafio.globo.service.NotificationSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class NotificationController {

    private final NotificationSender sender;

    @PostMapping(path = "/notifications",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Notification> send(@RequestBody Notification notification) {
        if (notification == null || notification.subscription() == null || notification.type() == null) {
            log.error("The request body is not valid {}.", notification);
            return new ResponseEntity<>(notification, HttpStatus.BAD_REQUEST);
        }
        sender.send(notification);
        log.info("The request is accepted {}.", notification);

        return new ResponseEntity<>(notification, HttpStatus.ACCEPTED);
    }
}
