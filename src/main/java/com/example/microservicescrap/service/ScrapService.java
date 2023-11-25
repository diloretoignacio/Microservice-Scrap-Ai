package com.example.microservicescrap.service;

import com.example.microservicescrap.entity.ScrapResult;
import com.example.microservicescrap.repository.ScrapResultRepository;
import com.example.microservicescrap.response.CheckResponse;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.microservicescrap.security.TokenValidator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.microservicescrap.request.CheckRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScrapService {
    private final TokenValidator tokenValidator;
    private final ScrapResultRepository scrapResultRepository;

    @Value("${scrap.api-key}")
    private String apiKey;

    @Value("${scrap.api-url}")
    private String apiUrl;

    @Autowired
    public ScrapService(TokenValidator tokenValidator, ScrapResultRepository scrapResultRepository) {
        this.tokenValidator = tokenValidator;
        this.scrapResultRepository = scrapResultRepository;
    }

    public ResponseEntity checkScrap(String authorizationHeader, CheckRequest checkRequest) throws URISyntaxException, IOException, InterruptedException {
        String jwt = authorizationHeader.substring(7);

        if (tokenValidator.validarToken(jwt)) {
            // Token válido
            Claims claims = tokenValidator.getClaim(jwt);
            String user = claims.getSubject();

            // Llamada a la API externa para obtener los resultados de scrap
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

            // La primera parte es el código
            String classScrapCode = parts[0].trim();

            // La segunda parte es el nombre
            String classScrapName = parts[1].trim();

            int kg = checkRequest.getKg();

            // Obtener la fecha y hora actual
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Formatear la fecha y hora como un String
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);


            ScrapResult scrapResult = new ScrapResult();
            scrapResult.setClassScrapCode(classScrapCode);
            scrapResult.setClassScrapName(classScrapName);
            scrapResult.setConfidence(confidence);
            scrapResult.setKg(kg);
            scrapResult.setDate(formattedDateTime);
            scrapResult.setUsername(user);

            // Llamada al repositorio para guardar el resultado
            scrapResultRepository.save(scrapResult);

            // Devolver la respuesta
            return ResponseEntity.ok(new CheckResponse(classScrapCode, classScrapName, confidence, kg, formattedDateTime));
        } else {
            // Token no válido
            String mensajeError = "Token no válido. Debe iniciar sesión nuevamente";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensajeError);
        }
    }

    public ResponseEntity getAllScrapResults(String authorizationHeader) throws URISyntaxException, IOException, InterruptedException {
        String jwt = authorizationHeader.substring(7);

        if (tokenValidator.validarToken(jwt)) {
            // Token válido
            Claims claims = tokenValidator.getClaim(jwt);
            // Más lógica de negocio si es necesario...

            // Llamada al repositorio para obtener todos los resultados
            List<ScrapResult> scrapResults = scrapResultRepository.findAll();

            // Devolver la respuesta
            return ResponseEntity.ok(scrapResults);
        } else {
            // Token no válido
            String mensajeError = "Token no válido. Debe iniciar sesión nuevamente";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensajeError);
        }
    }

    public ResponseEntity<List<ScrapResult>> getScrapResultsByUsername(String authorizationHeader, String username) throws URISyntaxException, IOException, InterruptedException {
        String jwt = authorizationHeader.substring(7);

        if (tokenValidator.validarToken(jwt)) {
            // Token válido
            Claims claims = tokenValidator.getClaim(jwt);
            // Más lógica de negocio si es necesario...

            // Llamada al repositorio para obtener resultados por nombre de usuario
            List<ScrapResult> scrapResults = scrapResultRepository.findByUsername(username);

            // Devolver la respuesta
            return ResponseEntity.ok(scrapResults);
        } else {
            // Token no válido
            String mensajeError = "Token no válido. Debe iniciar sesión nuevamente";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
