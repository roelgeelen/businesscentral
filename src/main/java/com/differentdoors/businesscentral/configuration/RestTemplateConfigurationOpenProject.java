package com.differentdoors.businesscentral.configuration;

import com.differentdoors.businesscentral.exceptions.CustomResponseErrorHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfigurationOpenProject {
    @Value("${different_doors.business_central.token_url}")
    protected String accessTokenUri;
    @Value("${different_doors.business_central.client_id}")
    protected String clientId;
    @Value("${different_doors.business_central.client_secret}")
    protected String clientSecret;
    @Value("${different_doors.business_central.scope}")
    protected String scope;

    @Bean
    protected ClientCredentialsResourceDetails oAuthDetails() {
        ClientCredentialsResourceDetails c = new ClientCredentialsResourceDetails();
        c.setAccessTokenUri(accessTokenUri);
        c.setClientId(clientId);
        c.setClientSecret(clientSecret);
        c.setScope(List.of(scope));
        c.setTokenName("Delica");
        return c;
    }

    @Bean(name = "BusinessCentral")
    public RestTemplate restTemplate() throws Exception {
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(oAuthDetails());
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(15000);
        requestFactory.setReadTimeout(15000);
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        requestFactory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        return restTemplate;
    }
}
