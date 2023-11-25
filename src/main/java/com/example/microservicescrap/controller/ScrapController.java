package com.example.microservicescrap.controller;

import com.example.microservicescrap.entity.ScrapResult;
import com.example.microservicescrap.security.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.microservicescrap.request.CheckRequest;
import java.net.URISyntaxException;
import java.io.IOException;
import java.util.List;
import com.example.microservicescrap.service.ScrapService;

@RestController
public class ScrapController {
    private final TokenValidator tokenValidator;
    private final ScrapService scrapService;

    @Autowired
    public ScrapController(TokenValidator tokenValidator, ScrapService scrapService) {
        this.tokenValidator = tokenValidator;
        this.scrapService = scrapService;
    }

    @PostMapping("/check")
    @CrossOrigin
    public ResponseEntity check(@RequestHeader(name = "Authorization") String authorizationHeader, @RequestBody CheckRequest checkRequest) throws URISyntaxException, IOException, InterruptedException {
        return scrapService.checkScrap(authorizationHeader, checkRequest);
    }

    @GetMapping("/check/all")
    @CrossOrigin
    public ResponseEntity getAllScrapResults(@RequestHeader(name = "Authorization") String authorizationHeader) throws URISyntaxException, IOException, InterruptedException {
        return scrapService.getAllScrapResults(authorizationHeader);
    }

    @GetMapping("/check/{username}")
    @CrossOrigin
    public ResponseEntity<List<ScrapResult>> getScrapResultsByUsername(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable String username
    ) throws URISyntaxException, IOException, InterruptedException {
        return scrapService.getScrapResultsByUsername(authorizationHeader, username);
    }

    @CrossOrigin
    @RequestMapping(value = "/check", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptionsRequest() {
        return ResponseEntity.ok().build();
    }
}
