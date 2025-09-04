package com.berkayd.microservices.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.berkayd.microservices.order.client.InventoryClient;

@Configuration
public class RestClientConfig {
    
    @Value("${inventory.url}")
    private String inventoryServiceUrl;

    @Bean
    public InventoryClient inventoryClient() {
        RestClient rest = RestClient.builder().baseUrl(inventoryServiceUrl).build();

        RestClientAdapter rca = RestClientAdapter.create(rest);
        HttpServiceProxyFactory hspf = HttpServiceProxyFactory.builderFor(rca).build();
        return hspf.createClient(InventoryClient.class);
    }
}
