package com.svalero.ermreactive.service;

import com.svalero.ermreactive.domain.Incident;
import com.svalero.ermreactive.exception.IncidentNotFoundException;
import com.svalero.ermreactive.repository.IncidentRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    private final Logger logger = LoggerFactory.getLogger(IncidentServiceImpl.class);

    @Override
    public Flux<Incident> findAll() {
        return incidentRepository.findAll();
    }

    @Override
    public Mono<Incident> findById(String id) throws IncidentNotFoundException {
        return incidentRepository.findById(id).switchIfEmpty(Mono.defer(() -> Mono.error(new IncidentNotFoundException())));
    }

    @Override
    public Flux<Incident> findByLocation(String location) {
        return incidentRepository.findByLocationContaining(location);
    }

    @Override
    public Flux<Incident> findByStatus(int status) {
        return incidentRepository.findByStatus(status);
    }

    @Override
    public Flux<Incident> findByDescriptionContainingIgnoreCase(String description) {
        return incidentRepository.findByDescriptionContainingIgnoreCase(description);
    }

    @Override
    public Mono<Incident> addIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    @Override
    public Mono<Incident> deleteIncident(String id) throws IncidentNotFoundException {
        Mono<Incident> existingIncident = incidentRepository.findById(id).switchIfEmpty(Mono.defer(() -> Mono.error(new IncidentNotFoundException())));
        incidentRepository.deleteById(id).block();
        return existingIncident;
    }

    @Override
    public Mono<Incident> modifyIncident(String id, Incident incident) throws IncidentNotFoundException {
        Mono<Incident> existingIncident = incidentRepository.findById(id).switchIfEmpty(Mono.defer(() -> Mono.error(new IncidentNotFoundException())));
        Incident newIncident = existingIncident.block();
        ModelMapper mp = new ModelMapper();
        newIncident = mp.map(incident, Incident.class);
        newIncident.setId(id);
        return incidentRepository.save(newIncident);

    }


}
