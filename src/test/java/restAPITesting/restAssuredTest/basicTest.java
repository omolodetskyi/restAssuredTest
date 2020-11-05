package restAPITesting.restAssuredTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;


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

	@Ignore @Test
	public void addAndDeletePet(){
		int petId = 2;
		AddPet requestBody = new AddPet();
		requestBody.setId(3);
		Category category = new Category();
		category.setId(0);
		category.setName("animals");
		requestBody.setCategory(category);
		String name = "Murzik";
		requestBody.setName(name);
		List<String> photoUrls = new ArrayList<String>();
		photoUrls.add("newphoto.jpg");
		requestBody.setPhotoUrls(photoUrls);
		List<Tags> tags = new ArrayList<Tags>();
		Tags tag1 = new Tags();
		tag1.setId(0);
		tag1.setName("cats");
		Tags tag2 = new Tags();
		tag2.setId(1);
		tag2.setName("homeless");
		tags.add(tag1);
		tags.add(tag2);
		requestBody.setTags(tags);
		String status = "available";
		requestBody.setStatus(status);
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(requestBody);
		System.out.println(json);
		//		String requestBody = "{" +
		//				"\"id\":" + petId + "," +
		//				"\"category\": {" +
		//				"\"id\": 0," +
		//				"\"name\": \"animals\"" +
		//				"}," +
		//				"\"name\": \"Barsic\"," +
		//				"\"photoUrls\": [" +
		//				"\"string\"" +
		//				"]," +
		//				"\"tags\": [" +
		//				"{" +
		//				"\"id\": 0," +
		//				"\"name\": \"cat\"" +
		//				"}," +
		//				"{" +
		//				"\"id\": 1," +
		//				"\"name\": \"puffy\"" +
		//				"}" +
		//				"]," +
		//				"\"status\": \"available\"" +
		//				"}";
		given()
		.contentType(ContentType.JSON)
		.body(requestBody,ObjectMapperType.GSON)
		.when()
		.post("/pet")
		.then()
		.log()
		.body()
		.body("name", equalTo("Barsic"))
		.body("category.name", equalTo("animals"))
		.body("tags[1].name", equalTo("puffy"))
		.statusCode(200);
		given()
		.header("api_key","special_key")
		.pathParam("petId", petId)
		.when()
		.delete("/pet/{petId}")
		.then()
		.log()
		.all()
		.statusCode(200);
	}

	@Ignore @Test
	public void userLogin(){
		given()
		.queryParam("username", "test")
		.queryParam("password", "test")
		.when()
		.get("/user/login")
		.then()
		.header("x-rate-limit", equalTo("5000"))
		.log()
		.all()
		.statusCode(200);
	}

	@Test
	public void getPet() {

		Response res = given()
				.pathParam("id",3)
				.when()
				.get("/pet/{id}")
				.then()
				.extract()
				.response();
		String jsonResponse = res.getBody().asString();
		AddPet requestBody = new Gson().fromJson(jsonResponse, AddPet.class);
		System.out.println(requestBody.getId());
		System.out.println(requestBody.getName());

	}
}
