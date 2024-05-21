package com.nighthawk.spring_portfolio.mvc.generalization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GeneralizationService {
    private final GeneralizationRepository generalizationRepository;

    @Autowired
    public GeneralizationService(GeneralizationRepository generalizationRepository) {
        this.generalizationRepository = generalizationRepository;
    }

    public Generalization saveGeneralization(String prediction) {
        Generalization newGeneralization = new Generalization(prediction, LocalDateTime.now());
        return generalizationRepository.save(newGeneralization);
    }

    public List<Generalization> getAllGeneralizations() {
        return generalizationRepository.findAll();
    }
}
