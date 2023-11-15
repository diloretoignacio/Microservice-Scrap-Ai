package com.example.microservicescrap.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class ScrapResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;

    private String classScrapCode;
    private String classScrapName;
    private double confidence;
    private int kg;

    // Getter y Setter para id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Getter y Setter para el user
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter y Setter para classScrapCode
    public String getClassScrapCode() {
        return classScrapCode;
    }

    public void setClassScrapCode(String classScrapCode) {
        this.classScrapCode = classScrapCode;
    }

    // Getter y Setter para classScrapName
    public String getClassScrapName() {
        return classScrapName;
    }

    public void setClassScrapName(String classScrapName) {
        this.classScrapName = classScrapName;
    }

    // Getter y Setter para confidence
    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    // Getter y Setter para kg
    public int getKg() {
        return kg;
    }

    public void setKg(int kg) {
        this.kg = kg;
    }
}
