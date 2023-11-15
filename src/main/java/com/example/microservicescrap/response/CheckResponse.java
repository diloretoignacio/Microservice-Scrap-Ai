package com.example.microservicescrap.response;

public class CheckResponse {
    private String classScrapCode;
    private String classScrapName;

    private double confidence;
    private int kg;

    // Constructor vacío (necesario para la deserialización JSON)
    public CheckResponse() {
    }

    // Constructor con parámetros
    public CheckResponse(String classScrapCode, String classScrapName, double confidence, int kg) {
        this.confidence = confidence;
        this.kg = kg;
        this.classScrapCode = classScrapCode;
        this.classScrapName = classScrapName;
    }

    // Getters y setters para confidence
    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    // Getters y setters para kg
    public int getKg() {
        return kg;
    }

    public void setKg(int kg) {
        this.kg = kg;
    }

    // Getters y setters para classScrapCode
    public String getClassScrapCode() {
        return classScrapCode;
    }

    public void setClassScrapCode(String classScrapCode) {
        this.classScrapCode = classScrapCode;
    }

    // Getters y setters para classScrapName
    public String getClassScrapName() {
        return classScrapName;
    }

    public void setClassScrapName(String classScrapName) {
        this.classScrapName = classScrapName;
    }
}
