package com.mdud.sternumauth.cdn;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class CDNConfiguration {

    @Value("${spring.sternum.auth.cdn.address}")
    private String cdnAddress;

    @Bean("cdnTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(cdnAddress))
                .build();
    }
}
