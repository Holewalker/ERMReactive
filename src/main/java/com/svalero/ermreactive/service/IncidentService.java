package com.svalero.ermreactive.service;

import com.svalero.ermreactive.domain.Incident;
import com.svalero.ermreactive.exception.IncidentNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IncidentService {

    Flux<Incident> findAll();

    Mono<Incident> findById(String id) throws IncidentNotFoundException;

    Flux<Incident> findByLocation(String location);

    Flux<Incident> findByStatus(int status);

    Flux<Incident> findByDescriptionContainingIgnoreCase(String description);

    Mono<Incident> addIncident(Incident incident);

    Mono<Incident> deleteIncident(String id) throws IncidentNotFoundException;

    Mono<Incident> modifyIncident(String id, Incident incident) throws IncidentNotFoundException;
}
