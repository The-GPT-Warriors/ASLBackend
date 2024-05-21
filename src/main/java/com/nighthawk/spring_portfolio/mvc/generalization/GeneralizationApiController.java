package com.nighthawk.spring_portfolio.mvc.generalization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generalizations")
public class GeneralizationApiController {
    private final GeneralizationService generalizationService;

    @Autowired
    public GeneralizationApiController(GeneralizationService generalizationService) {
        this.generalizationService = generalizationService;
    }

    @GetMapping
    public List<Generalization> getAllGeneralizations() {
        return generalizationService.getAllGeneralizations();
    }

    @PostMapping
    public Generalization saveGeneralization(@RequestBody String prediction) {
        return generalizationService.saveGeneralization(prediction);
    }
}
