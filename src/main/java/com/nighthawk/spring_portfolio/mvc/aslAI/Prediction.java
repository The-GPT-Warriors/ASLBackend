package com.nighthawk.spring_portfolio.mvc.aslAI;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // This annotation specifies that the class is an entity and is mapped to a database table.
public class Prediction {

    @Id // This annotation specifies the primary key of an entity.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This annotation specifies the strategy for generating the primary key values.
    private Long id; // Represents the primary key column of the table.

    private String predictionData; // Represents a column in the table that stores the prediction data.

    // Standard getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPredictionData() {
        return predictionData;
    }

    public void setPredictionData(String predictionData) {
        this.predictionData = predictionData;
    }
}