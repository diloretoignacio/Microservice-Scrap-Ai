package com.example.microservicescrap.repository;

import com.example.microservicescrap.entity.ScrapResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapResultRepository extends JpaRepository<ScrapResult, Long> {
    List<ScrapResult> findByUsername(String username);
}
