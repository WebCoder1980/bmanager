package com.bmanager.gateway.client;

import com.bmanager.gateway.dto.JwtDataResponse;
import com.bmanager.gateway.dto.ResultContainer;
import com.bmanager.gateway.dto.UserResponse;
import com.bmanager.gateway.util.MicroservicesPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class UserClient {
    private final RestTemplate restTemplate;

    @Autowired
    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserResponse getById(Long id) {
        UserResponse result = null;
        ResultContainer<UserResponse> resultContainer = null;

        String url = String.format(MicroservicesPath.USERS.getPath() + "/users/%d", id);

        String userJson = String.format("{\"id\": %d, \"email\": \"\", \"password\": \"\", \"username\": \"\"}", id);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-user", userJson);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<ResultContainer<UserResponse>>() {});

        resultContainer = (ResultContainer<UserResponse>)response.getBody();
        result = resultContainer.getContent();

        return result;
    }
}
