package com.mdud.sternumauth.cdn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class CDNServiceImpl implements CDNService {

    private final RestTemplate restTemplate;

    @Autowired
    public CDNServiceImpl(@Qualifier("cdnTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private void throwIfFileIsNotImage(byte[] image) {
        ByteArrayInputStream stream = new ByteArrayInputStream(image);

        try {
            if(ImageIO.read(stream) == null) {
                throw new IOException();
            }
        } catch (IOException e) {
            throw new ImageException();
        }
    }

    @Override
    public String addImage(byte[] image) {
        throwIfFileIsNotImage(image);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDispositionFormData("resource", "test.png");
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        params.add("resource", image);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange("/add", HttpMethod.POST, request, String.class);

        System.out.println(response.getBody());
        return null;
    }
}
