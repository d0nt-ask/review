package io.whatap.order.order.proxy;

import io.whatap.order.order.proxy.req.DecreaseInventoryQuantityRequest;
import io.whatap.order.order.proxy.res.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class ProductProxy {
    private final RestTemplate restTemplate;

    public ProductProxy(RestTemplateBuilder builder, @Value("${proxy.product:}") String baseUri) {
        this.restTemplate = builder.rootUri(baseUri).build();
    }

    public List<Long> decreaseInventoryQuantities(List<DecreaseInventoryQuantityRequest> requests) {
        return restTemplate.exchange("/inventory/decrease", HttpMethod.POST, new HttpEntity<>(requests), new ParameterizedTypeReference<List<Long>>() {
        }).getBody();
    }

    public ProductDto retrieveProduct(Long id) {
        return restTemplate.exchange("/product/{id}", HttpMethod.GET, null, ProductDto.class, Map.of("id", id)).getBody();
    }

}
