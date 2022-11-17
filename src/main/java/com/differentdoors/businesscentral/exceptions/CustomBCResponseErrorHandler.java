package com.differentdoors.businesscentral.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


@Component
public class CustomBCResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        HttpStatus status = clientHttpResponse.getStatusCode();
        return status.is4xxClientError() || status.is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        throw new CustomException(toString(clientHttpResponse.getBody()));
    }

    private String toString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    static class CustomException extends IOException {
        public CustomException(String message) {
            super(message);
        }
    }
}
