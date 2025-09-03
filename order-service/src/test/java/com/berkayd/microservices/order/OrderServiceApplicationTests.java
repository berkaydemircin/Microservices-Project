package com.berkayd.microservices.order;

import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class) // uses the single MySQLContainer bean
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
        "skuCode": "testing",
        "price": 100,
        "quantity": 401
      }
    """;

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
