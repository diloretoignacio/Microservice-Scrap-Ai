package com.example.microservicescrap.request;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckRequest {

    private String bytesImage;
    private int kg;

    // Constructor vacío (necesario para la deserialización JSON)
    public CheckRequest() {
    }

    // Constructor con parámetros
    public CheckRequest(@JsonProperty("bytesImage") String bytesImage, @JsonProperty("kg") int kg) {
        this.bytesImage = bytesImage;
        this.kg = kg;
    }

    // Getters y setters para bytesImage
    public String getBytesImage() {
        return bytesImage;
    }

    public void setBytesImage(String bytesImage) {
        this.bytesImage = bytesImage;
    }

    // Getters y setters para kg
    public int getKg() {
        return kg;
    }

    public void setKg(int kg) {
        this.kg = kg;
    }
}
