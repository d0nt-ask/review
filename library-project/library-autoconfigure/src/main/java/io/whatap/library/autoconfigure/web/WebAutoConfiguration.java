package io.whatap.library.autoconfigure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.whatap.library.shared.web.client.ExtendedDefaultResponseErrorHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@AutoConfiguration
@Import({DefaultRestControllerAdvice.class})
public class WebAutoConfiguration {
    @Bean
    public RestTemplateCustomizer restTemplateCustomizer(ObjectMapper objectMapper) {
        return restTemplate -> {

            PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
            CloseableHttpClient client = HttpClientBuilder.create()
                    .setConnectionManager(poolingConnManager)
                    .build();
//            HttpClientConnectionManager
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(client);

            restTemplate.setErrorHandler(new ExtendedDefaultResponseErrorHandler(objectMapper));
            restTemplate.setRequestFactory(requestFactory);
        };
    }

}
