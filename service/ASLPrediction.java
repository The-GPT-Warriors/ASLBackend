package com.nighthawk.spring_portfolio.mvc.aslAI.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.nighthawk.spring_portfolio.mvc.aslAI.PredictionService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication
public class ASLPrediction {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PredictionService predictionService) {
        return args -> predictionService.fetchDataAndPredict();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

class Prediction {

    private final RestTemplate restTemplate;

    @Autowired
    public PredictionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void fetchDataAndPredict() throws IOException {
        List<List<Integer>> testData = getPredictionData();
        Object predictionResult = predictionLogic(testData);
        postPrediction(predictionResult);
    }

    private Object predictionLogic(List<List<Integer>> mnistData) throws IOException {
        List<List<Integer>> data = readCSV("sign_mnist_train.csv");
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

    List<List<Integer>> getPredictionData() throws IOException {
        return readCSV("sign_mnist_test.csv"); // Assuming you have a similar test file
    }

    private void postPrediction(Object prediction) {
        System.out.println("Prediction: " + prediction);
        // Here, you would have logic to POST this prediction to a REST endpoint or process it as needed.
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
}
