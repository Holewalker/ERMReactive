package com.svalero.ermreactive.repository;

import com.svalero.ermreactive.domain.Incident;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IncidentRepository extends ReactiveMongoRepository<Incident, String> {
    @Override
    Flux<Incident> findAll();

    Flux<Incident> findByLocationContaining(String location);

    Flux<Incident> findByStatus(int status);

    Flux<Incident> findByDescriptionContainingIgnoreCase(String description);

    Mono<Incident> findById(String id);

}
