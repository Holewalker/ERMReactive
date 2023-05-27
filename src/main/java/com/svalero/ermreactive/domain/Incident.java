package com.svalero.ermreactive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Incident {
    @Id
    private String id;
    @Field
    private String location;
    @Field
    private double lat;
    @Field
    private double lon;
    @Field
    private int status;
    @Field
    private String description;
    @Field
    private LocalDate startTime;
}
