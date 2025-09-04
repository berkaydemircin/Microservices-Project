package com.berkayd.microservices.order.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
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
        RestClient rest = RestClient.builder().baseUrl(inventoryServiceUrl).requestFactory(getClientRequestFactory()).build();

        RestClientAdapter rca = RestClientAdapter.create(rest);
        HttpServiceProxyFactory hspf = HttpServiceProxyFactory.builderFor(rca).build();
        return hspf.createClient(InventoryClient.class);
    }

    private ClientHttpRequestFactory getClientRequestFactory() {
        ClientHttpRequestFactorySettings s = ClientHttpRequestFactorySettings.DEFAULTS
            .withConnectTimeout(Duration.ofSeconds(3))
            .withReadTimeout(Duration.ofSeconds(3));
        return ClientHttpRequestFactories.get(s);
    }
}
