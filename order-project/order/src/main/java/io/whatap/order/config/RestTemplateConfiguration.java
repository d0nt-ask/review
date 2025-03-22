package io.whatap.order.config;


import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        return restTemplate -> {
            PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
            CloseableHttpClient client = HttpClientBuilder.create()
                    .setConnectionManager(poolingConnManager)
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(client);
            restTemplate.setRequestFactory(requestFactory);
        };
    }
}
