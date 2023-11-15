package com.example.microservicescrap.controller;

import com.example.microservicescrap.entity.ScrapResult;
import com.example.microservicescrap.repository.ScrapResultRepository;
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
import java.util.List;

@RestController
public class ScrapController {
    private final TokenValidator tokenValidator;

    @Autowired
    public ScrapController(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Autowired
    private ScrapResultRepository scrapResultRepository;

    @PostMapping("/check")
    @CrossOrigin
    public ResponseEntity check(@RequestHeader(name = "Authorization") String authorizationHeader, @RequestBody CheckRequest checkRequest) throws URISyntaxException, IOException, InterruptedException {

        String jwt = authorizationHeader.substring(7); // Elimina "Bearer " del encabezado

        if (tokenValidator.validarToken(jwt)) {
            //Token valido
            Claims claims = tokenValidator.getClaim(jwt);
            String user = claims.getSubject();

            String apiKey = "YSnV9H1O85C19cTrM0rR";
            String apiUrl = "https://classify.roboflow.com/dalmine-ai-scrap/1";

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

            // Dividir la cadena en dos partes usando el espacio en blanco y el guion como delimitadores
            String[] parts = classScrap.split("\\s*-\\s*");

            // La primera parte es el codigo
            String classScrapCode = parts[0].trim();

            // La segunda parte es el nombre
            String classScrapName = parts[1].trim();

            int kg = checkRequest.getKg();

            ScrapResult scrapResult = new ScrapResult();
            scrapResult.setClassScrapCode(classScrapCode);
            scrapResult.setClassScrapName(classScrapName);
            scrapResult.setConfidence(confidence);
            scrapResult.setKg(kg);
            scrapResult.setUsername(user);

            scrapResultRepository.save(scrapResult);

            ResponseEntity<CheckResponse> successResponse = ResponseEntity.ok(new CheckResponse(classScrapCode, classScrapName, confidence, kg));
            return successResponse;
        } else {
            // Token no válido
            String mensajeError = "Token no válido. Debe iniciar sesion nuevamente";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensajeError);
        }
    }




    @GetMapping("/check/all")
    @CrossOrigin
    public ResponseEntity getAllScrapResults(@RequestHeader(name = "Authorization") String authorizationHeader) throws URISyntaxException, IOException, InterruptedException {

        String jwt = authorizationHeader.substring(7); // Elimina "Bearer " del encabezado

        if (tokenValidator.validarToken(jwt)) {
            //Token valido
            Claims claims = tokenValidator.getClaim(jwt);
            String user = claims.getSubject();


            List<ScrapResult> scrapResults = scrapResultRepository.findAll();
            return ResponseEntity.ok(scrapResults);
        } else {
            // Token no válido
            String mensajeError = "Token no válido. Debe iniciar sesion nuevamente";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensajeError);
        }
    }




    @GetMapping("/check/{username}")
    @CrossOrigin
    public ResponseEntity getScrapResultsByUsername(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable String username
    ) throws URISyntaxException, IOException, InterruptedException {

        String jwt = authorizationHeader.substring(7);

        if (tokenValidator.validarToken(jwt)) {
            // Token válido
            Claims claims = tokenValidator.getClaim(jwt);
            String loggedInUser = claims.getSubject();

            List<ScrapResult> scrapResults = scrapResultRepository.findByUsername(username);
            return ResponseEntity.ok(scrapResults);
        } else {
            // Token no válido
            String mensajeError = "Token no válido. Debe iniciar sesión nuevamente";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensajeError);
        }
    }


}
