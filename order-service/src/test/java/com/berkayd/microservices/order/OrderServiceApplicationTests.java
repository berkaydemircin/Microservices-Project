package com.berkayd.microservices.order;

import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import com.berkayd.microservices.order.stubs.InventoryClientStub;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class) 
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
  "inventory.url=http://localhost:${wiremock.server.port}",
  "spring.cloud.discovery.enabled=false"
})
class OrderServiceApplicationTests {

  @LocalServerPort
  int port;

  @BeforeEach
  void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  @Test
  void shouldCreateOrder() {
    String reqBody = """
      {
        "skuCode": "iphone_15",
        "price": 1000,
        "quantity": 1
      }
    """;

    InventoryClientStub.stubInventoryCall("iphone_15", 1);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(reqBody)
        .when()
        .post("/api/order")
        .then()
        .statusCode(201)
        .contentType(ContentType.TEXT)
        .body(equalTo("Order placed successfully"));
  }
}
