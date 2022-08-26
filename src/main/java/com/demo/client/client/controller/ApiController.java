package com.demo.client.client.controller;

import com.demo.client.client.dto.UserResponse;
import com.demo.client.client.service.RestTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ApiController {
    private final RestTemplateService restTemplateService;

    public ApiController(RestTemplateService restTemplateService) {
        this.restTemplateService=restTemplateService;
    }

    @GetMapping("/hello")
    public UserResponse getHello() {
        return restTemplateService.hello();
    }

    @GetMapping("/post")
    public UserResponse post() {
        return restTemplateService.post();
    }

    @GetMapping("/exchange")
    public ResponseEntity exchange() {
        return restTemplateService.exchange();
    }

    @GetMapping("/naver")
    public ResponseEntity naver() {
        return restTemplateService.naver();
    }
}
