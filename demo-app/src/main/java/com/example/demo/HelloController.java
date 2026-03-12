package com.example.demo;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${spring.application.name:demo-app}")
    private String appName;

    @Value("${app.version:0.0.1}")
    private String appVersion;

    @GetMapping("/")
    public String home() {
        return "demo-app is running!";
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("time", LocalDateTime.now().toString());
        return response;
    }

    @GetMapping("/api/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("app", appName);
        response.put("version", appVersion);
        response.put("message", "DevOps practice project for Jenkins + ArgoCD + Helm");
        return response;
    }

    @GetMapping("/test")
    public String test() {
        return "추가 배포 성공!";
    }
}
