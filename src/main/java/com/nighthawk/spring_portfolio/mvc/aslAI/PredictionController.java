package com.nighthawk.spring_portfolio.mvc.aslAI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class PredictionController {

    @Autowired
    private PredictionRepository predictionRepository;

    @PostMapping("/aslresult")
    public ResponseEntity<String> postPrediction(@RequestBody String predictionData) {
        Prediction prediction = new Prediction();
        prediction.setPredictionData(predictionData);
        predictionRepository.save(prediction);

        return ResponseEntity.ok("Prediction saved successfully!");
    }


    @GetMapping("/prediction")
    public List<Prediction> getPredictionData() {
        return predictionRepository.findAll();
    }
}

