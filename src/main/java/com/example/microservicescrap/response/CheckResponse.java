package com.example.microservicescrap.response;

public class CheckResponse {

    private String classScrap;
    private double confidence;

    // Constructor vacío (necesario para la deserialización JSON)
    public CheckResponse() {
    }

    // Constructor con parámetros
    public CheckResponse(String classScrap, double confidence) {
        this.classScrap = classScrap;
        this.confidence = confidence;
    }

    // Getters y setters
    public String getClassScrap() {
        return classScrap;
    }

    public void setClassScrap(String classScrap) {
        this.classScrap = classScrap;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}

