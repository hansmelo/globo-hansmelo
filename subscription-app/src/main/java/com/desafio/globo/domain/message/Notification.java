package com.desafio.globo.domain.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Notification(@JsonProperty("notification_type") String type, String subscription
) {
}
