package com.bootcamptoprod.config;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

// Not required if you are using rest template with Apache Http client
public class GzipDecompressingClientHttpResponse implements ClientHttpResponse {

    private final ClientHttpResponse response;

    public GzipDecompressingClientHttpResponse(ClientHttpResponse response) {
        this.response = response;
    }

    @Override
    public InputStream getBody() throws IOException {
        InputStream body = response.getBody();
        if (isGzipped(response)) {
            return new GZIPInputStream(new BufferedInputStream(body));
        }
        return body;
    }

    private boolean isGzipped(ClientHttpResponse response) {
        String contentEncoding = response.getHeaders().getFirst("Content-Encoding");
        return contentEncoding != null && contentEncoding.toLowerCase().contains("gzip");
    }


    @Override
    public HttpStatusCode getStatusCode() throws IOException {
        return response.getStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return response.getStatusText();
    }

    @Override
    public void close() {
        response.close();
    }

    @Override
    public org.springframework.http.HttpHeaders getHeaders() {
        return response.getHeaders();
    }
}
