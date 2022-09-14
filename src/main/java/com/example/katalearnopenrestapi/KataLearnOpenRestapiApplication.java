package com.example.katalearnopenrestapi;

import com.example.katalearnopenrestapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
public class KataLearnOpenRestapiApplication {
    private String apiUrl = "http://94.198.50.185:7081/api/users";
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(KataLearnOpenRestapiApplication.class, args);
    }

    @Autowired
    private void getRestTemplate(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Bean
    public void run() {
        StringBuilder succesToken = new StringBuilder();
        User user = new User(3L, "James", "Brown", (byte) 25);

        ResponseEntity<User[]> response1 = restTemplate.getForEntity(apiUrl, User[].class);;

        final String sessionId = response1.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionId);

        ResponseEntity<String> response2 = restTemplate.postForEntity(
                apiUrl,
                new HttpEntity<>(user, headers),
                String.class
        );
        succesToken.append(response2.getBody());

        user.setName("Thomas");
        user.setLastName("Shelby");
        ResponseEntity<String> response3 = restTemplate.exchange(
                apiUrl,
                HttpMethod.PUT,
                new HttpEntity<>(user, headers),
                String.class
        );
        succesToken.append(response3.getBody());

        ResponseEntity<String> response4 = restTemplate.exchange(
                apiUrl + "/" + user.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(user, headers),
                String.class
        );
        succesToken.append(response4.getBody());

        System.out.println(succesToken);
    }
}
