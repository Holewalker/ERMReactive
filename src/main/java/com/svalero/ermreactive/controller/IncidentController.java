package com.svalero.ermreactive.controller;

import com.svalero.ermreactive.domain.Incident;
import com.svalero.ermreactive.exception.ErrorException;
import com.svalero.ermreactive.exception.IncidentNotFoundException;
import com.svalero.ermreactive.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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


    @GetMapping("/incidents")
    public ResponseEntity<Flux<Incident>> getIncidents(@RequestParam Map<String, String> data) throws IncidentNotFoundException {

        if (data.isEmpty()) {
            return ResponseEntity.ok(incidentService.findAll().delayElements(Duration.ofSeconds(2)));
        } else if (data.containsKey("status")) {
            Flux<Incident> incidentList = incidentService.findByStatus(Integer.parseInt(data.get("status")));
            return ResponseEntity.ok(incidentList);
        } else if (data.containsKey("location")) {
            Flux<Incident> incidentList = incidentService.findByLocation(data.get("location"));
            return ResponseEntity.ok(incidentList);
        } else if (data.containsKey("description")) {
            Flux<Incident> incidentList = incidentService.findByDescriptionContainingIgnoreCase(data.get("description"));
            return ResponseEntity.ok(incidentList);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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


