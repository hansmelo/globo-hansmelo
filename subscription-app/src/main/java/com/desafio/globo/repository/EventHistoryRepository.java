package com.desafio.globo.repository;

import com.desafio.globo.domain.model.EventHistory;
import com.desafio.globo.domain.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventHistoryRepository extends JpaRepository<EventHistory, Long> {
}
