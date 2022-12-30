package com.differentdoors.businesscentral.services;

import com.differentdoors.businesscentral.models.Job;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobService {
    @Value("${different_doors.business_central.url}")
    private String URL;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    @Autowired
    @Qualifier("BusinessCentral")
    private RestTemplate restTemplate;

    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Job getJob(String id) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "HUB_Projects('P" + id + "')");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        return restTemplate.getForObject(builder.buildAndExpand(urlParams).toUri(), Job.class);
    }

    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void createJob(Job job) throws Exception {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "HUB_Projects");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(objectMapper.writeValueAsString(job), headers);
        restTemplate.postForObject(builder.buildAndExpand(urlParams).toUri(), entity, String.class);
    }

    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void updateJob(String id, String etag, Job job) throws Exception {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "HUB_Projects('P" + id + "')");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("If-Match", etag);
        HttpEntity<Object> entity = new HttpEntity<>(objectMapper.writeValueAsString(job), headers);
        restTemplate.patchForObject(builder.buildAndExpand(urlParams).toUri(), entity, String.class);
    }

    @Recover
    public RetryException recover(Exception t){
        return new RetryException("Maximum retries reached: " + t.getMessage());
    }

}
