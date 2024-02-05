package com.nighthawk.spring_portfolio.mvc.frames;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Frame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String fileName;

    @Column
    private String imageEncoder;

    public Frame(String fileName, String imageEncoder) {
        this.fileName = fileName;
        this.imageEncoder = imageEncoder;
    }
}
