package com.example.microservicescrap.controller;

import com.example.microservicescrap.security.TokenValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.microservicescrap.request.CheckRequest;
import com.example.microservicescrap.response.CheckResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

@RestController
public class ScrapController {
    private final TokenValidator tokenValidator;

    @Autowired
    public ScrapController(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @PostMapping("/check")
    @CrossOrigin
    public ResponseEntity check(@RequestHeader(name = "Authorization") String authorizationHeader, @RequestBody CheckRequest checkRequest) throws URISyntaxException, IOException, InterruptedException {

        String jwt = authorizationHeader.substring(7); // Elimina "Bearer " del encabezado

        if (tokenValidator.validarToken(jwt)) {
            //Token valido
            Claims claims = tokenValidator.getClaim(jwt);
            String user = claims.getSubject();

            String apiKey = "xJPizhEVztPuOcfclwPM";
            String apiUrl = "https://classify.roboflow.com/audit-scrap-ai2/2";

            String base64Image = checkRequest.getBytesImage();
            String queryString = "api_key=" + apiKey;
            String urlWithParams = apiUrl + "?" + queryString;
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlWithParams))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(base64Image))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            // Utilizar Jackson para analizar el JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            // Extraer la información necesaria
            double confidence = jsonNode.get("confidence").asDouble();
            String classScrap = jsonNode.get("top").asText();

            ResponseEntity<CheckResponse> successResponse = ResponseEntity.ok(new CheckResponse(classScrap, confidence));
            return successResponse;
        } else {
            // Token no válido
            String mensajeError = "Token no válido. Debe iniciar sesion nuevamente";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensajeError);
        }
    }
}
