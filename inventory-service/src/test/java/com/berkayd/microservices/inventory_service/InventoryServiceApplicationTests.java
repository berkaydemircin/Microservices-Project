package com.berkayd.microservices.inventory_service;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import com.berkayd.microservices.inventory_service.model.Inventory;
import com.berkayd.microservices.inventory_service.repository.InventoryRepository;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	InventoryRepository repo;


	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
		repo.deleteAll();
		repo.saveAll(List.of(
			new Inventory(null, "phone", 100),
			new Inventory(null, "whatever", 105),
			new Inventory(null, "mybad", 90)
		));
	}

	 @Test
  void shouldReadInventory() {
    // positive: enough stock
    boolean ok = RestAssured.given()
        .when()
        .get("/api/inventory?skuCode=phone&quantity=1")
        .then()
        .statusCode(200)
        .extract().as(Boolean.class);
    assertTrue(ok);

    // negative: not enough stock
    boolean notOk = RestAssured.given()
        .when()
        .get("/api/inventory?skuCode=mybad&quantity=100")
        .then()
        .statusCode(200)
        .extract().as(Boolean.class);
    assertFalse(notOk);

    // another positive
    boolean ok2 = RestAssured.given()
        .when()
        .get("/api/inventory?skuCode=whatever&quantity=10")
        .then()
        .statusCode(200)
        .extract().as(Boolean.class);
    assertTrue(ok2);
  }
}