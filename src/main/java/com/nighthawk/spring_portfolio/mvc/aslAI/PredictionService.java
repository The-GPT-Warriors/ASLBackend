package com.nighthawk.spring_portfolio.mvc.aslAI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PredictionService {
    private final PredictionRepository predictionRepository;

    @Autowired
    public PredictionService(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    public String predictAndSave(List<List<Integer>> mnistData) {
        String predictionResult = predictionLogic(mnistData);
        savePrediction(predictionResult); // Save without needing to return the saved entity
        return predictionResult; // Return the result directly
    }

    private List<List<Integer>> readCSV(String fileName) throws IOException {
        List<List<Integer>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true; // To skip the header row
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header row
                }
                String[] values = line.split(",");
                List<Integer> row = new ArrayList<>();
                for (String val : values) {
                    try {
                        row.add(Integer.parseInt(val.trim()));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping non-numeric value: " + val);
                        // Handle the non-numeric value, or continue
                    }
                }
                data.add(row);
            }
        }
        return data;
    }

    // Assuming this is your prediction logic method
    private String predictionLogic(List<List<Integer>> mnistData) {
        List<List<Integer>> data = new ArrayList<>();
        try {
            data = readCSV("sign_mnist_train.csv");
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            // Handle the exception, e.g., log it or return a default value
            return "Error"; // or any other error handling mechanism
        }
        int lg = data.size();
        int lg1 = data.get(0).size();

        int[][] x = new int[lg - 1][lg1];
        for (int i = 0; i < lg - 1; i++) {
            for (int j = 0; j < lg1; j++) {
                x[i][j] = data.get(i + 1).get(j);
            }
        }

        double[][] weights = new double[25][lg1];
        int[] digit = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y"};
        int m = weights[0].length - 1;

        double rate = 0.5;
        int epoch = 5;
        for (int s = 0; s < 25; s++) {
            for (int ii = 0; ii < epoch; ii++) {
                double error = 0.0;
                for (int i = 0; i < x.length; i++) {
                    double y_pred = weights[s][0];
                    for (int k = 0; k < m; k++) {
                        y_pred += weights[s][k + 1] * x[i][k + 1];
                    }

                    double pred = y_pred >= 0.0 ? 1.0 : 0.0;
                    double expect = x[i][0] == digit[s] ? 1.0 : 0.0;
                    double err = pred - expect;
                    error += err * err;

                    weights[s][0] -= rate * err;
                    for (int k = 0; k < m; k++) {
                        weights[s][k + 1] -= rate * err * x[i][k + 1];
                    }
                }
                System.out.println("Letter: " + alphabet[s] + ", Epoch: " + ii + ", Error: " + error);
            }
        }

        List<List<Integer>> test = mnistData;
        int lgt = test.size();
        int lg1t = test.get(0).size() - 1;
        int s0 = 0;
        int[][] xt = new int[lgt - 1][lg1t];
        for (int i = 0; i < lgt - 1; i++) {
            for (int j = 0; j < lg1t; j++) {
                xt[i][j] = test.get(i + 1).get(j);
            }
        }
        double pred0 = -100000000;
        for (int ii = 0; ii < lgt - 1; ii++) {
            for (int s = 0; s < 25; s++) {
                double y_pred = weights[s][0];
                for (int k = 0; k < lg1t - 1; k++) {
                    y_pred += weights[s][k + 1] * xt[ii][k + 1];
                }
                if (y_pred > pred0) {
                    pred0 = y_pred;
                    s0 = s;
                }
            }
        }
        return alphabet[s0];
    }

    public Prediction savePrediction(String prediction) {
        Prediction newPrediction = new Prediction(prediction, LocalDateTime.now());
        return predictionRepository.save(newPrediction);
    }

    public List<Prediction> getAllPredictions() {
        return predictionRepository.findAll();
    }
}