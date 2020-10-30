package restAPITesting.restAssuredTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import io.restassured.RestAssured;


public class basicTest {
	private static final String BASE_URL = "https://petstore.swagger.io";
	private static final String BASE_PATH = "/v2";

	@BeforeClass
	public void setUp(){
		RestAssured.baseURI=BASE_URL;
		RestAssured.basePath=BASE_PATH;
	}

	//	given
	//	body
	// header
	// param
	//	when
	// get
	//post
	//	then
	//statusCode
	//body
	@Ignore @Test
	public void getPetByStatus(){
		given()
		.param("status","available")
		.when()
		.get("pet/findByStatus")
		.then()
		.log()
		.body()
		.statusCode(200);
	}

	@Test
	public void addPet(){
		String requestBody = "{" +
				"\"id\": 2," +
				"\"category\": {" +
				"\"id\": 0," +
				"\"name\": \"animals\"" +
				"}," +
				"\"name\": \"Barsic\"," +
				"\"photoUrls\": [" +
				"\"string\"" +
				"]," +
				"\"tags\": [" +
				"{" +
				"\"id\": 0," +
				"\"name\": \"cat\"" +
				"}," +
				"{" +
				"\"id\": 1," +
				"\"name\": \"puffy\"" +
				"}" +
				"]," +
				"\"status\": \"available\"" +
				"}";
		given()
		.contentType("application/json")
		.body(requestBody)
		.when()
		.post("/pet")
		.then()
		.log()
		.body()
		.body("name", equalTo("Barsic"))
		.body("category.name", equalTo("animals"))
		.body("tags[1].name", equalTo("puffy"))
		.statusCode(200);
	}
}
