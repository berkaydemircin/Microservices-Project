package com.berkayd.microservices.product;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT) // i do this not to block any already running ports
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mdb = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mdb.start();
	}

	@Test
	void shouldCreateProduct() {
		String reqBody = """
				{
					"name": "test",
					"description": "this is for testing",
					"price": 91.25
				}
			""";

		RestAssured.given()
						.contentType("application/json")
						.body(reqBody)
						.when()
						.post("/api/product")
						.then()
						.statusCode(201)
						.body("id", notNullValue())
						.body("name", equalTo("test"))
						.body("description", equalTo("this is for testing"))
						.body("price", equalTo(91.25F));
	}

}
