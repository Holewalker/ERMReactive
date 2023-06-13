package com.svalero.ermreactive.controller;

import com.svalero.ermreactive.domain.Incident;
import com.svalero.ermreactive.exception.ErrorException;
import com.svalero.ermreactive.exception.IncidentNotFoundException;
import com.svalero.ermreactive.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@RestController
public class IncidentController {

    @Autowired
    IncidentService incidentService;


    @GetMapping(value = "/incidents", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Incident> getIncidents(@RequestParam Map<String, String> data) throws IncidentNotFoundException {

        if (data.containsKey("status")) {
            return incidentService.findByStatus(Integer.parseInt(data.get("status")));
        } else if (data.containsKey("location")) {
            return incidentService.findByLocation(data.get("location"));
        } else if (data.containsKey("description")) {
            return incidentService.findByDescriptionContainingIgnoreCase(data.get("description"));
        } else
            return incidentService.findAll().delayElements(Duration.ofSeconds(2));

    }

    @GetMapping(value = "/incidents/{id}")
    public Mono<Incident> getProduct(@PathVariable String id) throws IncidentNotFoundException {
        Mono<Incident> incidentMono = incidentService.findById(id);
        return incidentMono.delayElement(Duration.ZERO);
    }

    @PostMapping("/incidents")
    public void addIncident(@RequestBody Incident incident) {
        incidentService.addIncident(incident).block();
    }

    @PutMapping("/incidents/{id}")
    public Mono<Incident> modifyIncident(@PathVariable String id, @RequestBody Incident incident) throws IncidentNotFoundException {
        Mono<Incident> incidentMono = incidentService.modifyIncident(id, incident);
        return ResponseEntity.status(HttpStatus.OK).body(incidentMono).getBody();
    }

    @DeleteMapping("incidents/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable String id) throws IncidentNotFoundException {
        Mono<Incident> incidentMono = incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(IncidentNotFoundException.class)
    public ResponseEntity<ErrorException> handleIncidentNotFoundException(IncidentNotFoundException enf) {
        ErrorException errorException = new ErrorException(404, enf.getMessage());
        return new ResponseEntity<>(errorException, HttpStatus.NOT_FOUND);
    }
}


