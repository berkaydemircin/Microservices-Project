package com.berkayd.microservices.order.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.berkayd.microservices.order.client.InventoryClient;
import com.berkayd.microservices.order.dto.OrderRequest;
import com.berkayd.microservices.order.model.Order;
import com.berkayd.microservices.order.event.OrderPlacedEvent;
import com.berkayd.microservices.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);


    public void placeOrder(OrderRequest orderRequest) {
        boolean flag = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (flag) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());

            orderRepository.save(order);

            OrderPlacedEvent e = new OrderPlacedEvent(order.getOrderNumber(), orderRequest.userDetails().email());
            log.info("Started sending OrderPlacedEvent {} to topic order-placed", e);
            kafkaTemplate.send("order-placed", e);
            log.info("Ended sending OrderPlacedEvent {} to topic order-placed", e);
        }
        else {
            throw new RuntimeException("Product wth sku code " + orderRequest.skuCode() + " is not in stock");
        }
    }
}
