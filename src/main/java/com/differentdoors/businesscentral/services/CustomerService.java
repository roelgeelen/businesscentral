package com.differentdoors.businesscentral.services;

import com.differentdoors.businesscentral.models.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerService {
    @Value("${different_doors.business_central.url}")
    private String URL;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    @Autowired
    @Qualifier("BusinessCentral")
    private RestTemplate restTemplate;

    public Customer getCustomer(String id) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "HUB_Customers('K" + id + "')");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        return restTemplate.getForObject(builder.buildAndExpand(urlParams).toUri(), Customer.class);
    }

    public void createCustomer(Customer customer) throws JsonProcessingException {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "HUB_Customers");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(objectMapper.writeValueAsString(customer), headers);
        restTemplate.postForObject(builder.buildAndExpand(urlParams).toUri(), entity, String.class);
    }

    public void updateCustomer(String id, String etag, Customer customer) throws JsonProcessingException {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "HUB_Customers('K" + id + "')");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("If-Match", etag);
        HttpEntity<Object> entity = new HttpEntity<>(objectMapper.writeValueAsString(customer), headers);
        restTemplate.patchForObject(builder.buildAndExpand(urlParams).toUri(), entity, String.class);
    }

}
