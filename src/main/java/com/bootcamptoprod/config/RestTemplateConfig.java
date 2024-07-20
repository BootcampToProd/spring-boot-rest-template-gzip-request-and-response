package com.bootcamptoprod.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        restTemplate.getInterceptors().add(gzipRequestInterceptor());
        restTemplate.getInterceptors().add(gzipResponseInterceptor()); // Not required if you are using rest template with Apache Http client

        // Uncomment below line if you want to decompress the rest template gzip response using Apache Http client
        // restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build()));

        return restTemplate;
    }

    private ClientHttpRequestInterceptor gzipRequestInterceptor() {
        return (request, body, execution) -> {
            request.getHeaders().add("Content-Encoding", "gzip");
            request.getHeaders().remove("Content-Length");
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
                gzipStream.write(body);
            }
            byte[] compressedBody = byteStream.toByteArray();
            return execution.execute(request, compressedBody);
        };
    }

    // Not required if you are using rest template with Apache Http client
    private ClientHttpRequestInterceptor gzipResponseInterceptor() {
        return (request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            return new GzipDecompressingClientHttpResponse(response);
        };
    }
}
