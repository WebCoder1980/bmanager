package com.bmanager.gateway.client;

import com.bmanager.gateway.dto.JwtDataResponse;
import com.bmanager.gateway.util.MicroservicesPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthClient {
    private final RestTemplate restTemplate;

    @Autowired
    public AuthClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JwtDataResponse getByToken(String token) {
        JwtDataResponse result = null;

        String url = String.format(MicroservicesPath.AUTH.getPath() + "/auth/token/decode?token=%s", token);
        result = restTemplate.getForObject(url, JwtDataResponse.class);

        return result;
    }
}
