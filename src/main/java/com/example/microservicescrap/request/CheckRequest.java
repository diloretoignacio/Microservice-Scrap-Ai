package com.example.microservicescrap.request;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckRequest {

    private String BytesImage;

    // Constructor vacío (necesario para la deserialización JSON)
    public CheckRequest() {
    }

    // Constructor con parámetros
    public CheckRequest(@JsonProperty("BytesImage") String BytesImage) {
        this.BytesImage = BytesImage;
    }

    // Getters y setters
    public String getBytesImage() {
        return BytesImage;
    }

    public void setBytesImage(String BytesImage) {
        this.BytesImage = BytesImage;
    }
}
