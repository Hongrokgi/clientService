package com.demo.client.client.service;

import com.demo.client.client.dto.UserRequest;
import com.demo.client.client.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
/*
*  ● RestTemplate 이란
*  ● 스프링에서 제공하는 http 통신에 유용하게 쓸 수 있는 템플릿
*  ● HTTP 서버와의 통신을 단순화하고 RESTful 원칙을 지킨다(json, xml을 쉽게 응답받음)
*
*  ● 동작원리
*  ● HttpClient 는 HTTP 를 사용하여 통신하는 범용 라이브러리
*  ● RestTemplate 는 HttpClient 를 추상화(HttpEntity 의 json, xml 등)해서 제공
*  1. 애플리케이션이 RestTemplate 를 생성하고, URI, HTTP 메서드 등의 헤더를 담아 요청
*  2. RestTemplate 는 HttpMessageConverter 를 사용하여 requestEntity 를 요청 메세지로 변환
*  3. RestTemplate 는 ClientHttpRequestFactory 로 부터 ClientHttpRequest 를 가져와서 요청을 보냄
*  4. ClientHttpRequest 는 요청 메시지를 만들어 HTTP 프로토콜을 통해 서버와 통신
*  5. RestTemplate 는 ResponseErrorHandler 로 오류를 확인하고 있다면 처리로직을 태움
*  6. ResponseErrorHandler 는 오류가 있다면 ClientHttpResponse 에서 응답데이터를 가져와서 처리
*  7. RestTemplate 는 HttpMessageConverter 를 이용해서 응답 메시지를 java Object(Class responseType)로 변환
*  8. 애플리케이션에 반환
*
* */
@Slf4j
@Service
public class RestTemplateService {
    // http://localhost/api/server/hello
    // response
    public UserResponse hello() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/hello")
                .queryParam("name","steve")
                .queryParam("age",13)
                .encode()
                .build()
                .toUri();

        System.out.println(uri.toString());

        RestTemplate restTemplate = new RestTemplate();
        // String result = restTemplate.getForObject(uri, String.class)
        // json 형태로 받자
        ResponseEntity<UserResponse> result = restTemplate.getForEntity(uri,UserResponse.class);
        // UserResponse result = restTemplate.getForObject(uri, UserResponse.class);

        System.out.println(result.getStatusCode());
        System.out.println(result.getBody());
        return result.getBody();
    }

    public UserResponse post() {
        // http://localhost:9090/api/server/user/{userId}/name/{userName}
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(11,"steve")
                .toUri();

        System.out.println("uri :" + uri);

        // http body -> object -> object mapper -> json -> rest template -> http body json
        UserRequest req = new UserRequest();
        req.setName("dsg");
        req.setAge(10);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(uri, req, UserResponse.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());

        return response.getBody();
    }

    public ResponseEntity exchange() {
        URI uri = UriComponentsBuilder
                .fromUriString("http;//localhost:9090")
                .path("/api/server/{path}/header")
                .encode()
                .build()
                .expand("user")
                .toUri();
        log.info("uri : {}", uri);

        UserRequest req = new UserRequest();
        req.setName("dsg");
        req.setAge(27);

        RequestEntity<UserRequest> request = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization", "my-header")
                .body(req);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {});
        log.info("{}",response.getStatusCode());
        log.info("{}",response.getHeaders());
        log.info("{}",response.getBody());

        return response;
    }

    public ResponseEntity naver() {
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query","%EC%A3%BC%EC%8B%9D")
                .queryParam("display","10")
                .queryParam("start","1")
                .queryParam("sort","random")
                .encode()
                .build()
                .toUri();
        log.info("uri : {}", uri);

        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id","Zi3o1uQftp59zuIqEAz4")
                .header("X-Naver-Client-Secret","iy6YKSWpLM")
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(req, new ParameterizedTypeReference<>() {});
        log.info("{}",response.getStatusCode());
        log.info("{}",response.getHeaders());
        log.info("{}",response.getBody());

        return response;
    }
}
