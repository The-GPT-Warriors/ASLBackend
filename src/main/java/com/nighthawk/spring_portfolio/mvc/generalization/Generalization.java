package com.nighthawk.spring_portfolio.mvc.generalization;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "generalizations")
public class Generalization {
    @Id
    private String id;
    private String prediction;
    private LocalDateTime timestamp;

    public Generalization(String prediction, LocalDateTime timestamp) {
        this.prediction = prediction;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getPrediction() {
        return prediction;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
