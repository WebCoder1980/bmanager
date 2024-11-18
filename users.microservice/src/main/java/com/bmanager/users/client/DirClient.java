package com.bmanager.users.client;

import com.bmanager.users.dto.DirPost;
import com.bmanager.users.model.DirModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DirClient {
    private final RestTemplate restTemplate;

    @Autowired
    public DirClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DirModel addDir(DirPost dir) {
        DirModel result = null;

        String url = "http://127.0.0.1:36001/dirs";
        result = restTemplate.postForObject(url, dir, DirModel.class);

        return result;
    }
}
