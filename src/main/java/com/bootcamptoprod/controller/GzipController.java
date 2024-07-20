package com.bootcamptoprod.controller;

import com.bootcamptoprod.dto.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GzipController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/gzipRequestResponse")
    public Employee gzipRequestResponse() {
        String url = "http://localhost:8080/decompress";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Encoding", "gzip");
        headers.add("Accept-Encoding", "gzip");

        Employee employee = new Employee(1, "John Doe");
        HttpEntity<Employee> entity = new HttpEntity<>(employee, headers);

        ResponseEntity<Employee> response = restTemplate.exchange(url, HttpMethod.POST, entity, Employee.class);

        System.out.println("Rest template response headers");
        response.getHeaders().forEach((s, strings) -> {
            System.out.println("Header name and value: " + s + " : " + strings);
        });

        return response.getBody();
    }
}
