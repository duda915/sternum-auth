package com.mdud.sternumauth.security;

import com.mdud.sternumauth.user.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${spring.sternum.client.id}")
    private String clientId;

    @Value("${spring.sternum.client.secret}")
    private String clientSecret;

    @Value("${spring.sternum.redirectUri}")
    private String redirectUri;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(clientId)
                .secret("{noop}" + clientSecret)
                .scopes("all")
                .authorizedGrantTypes("authorization_code")
                .redirectUris(redirectUri);
    }
}
