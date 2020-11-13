package restAPITesting.restAssuredTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.params.CoreConnectionPNames;
import org.awaitility.Awaitility;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import specifications.RequestSpecifications;
import specifications.ResponseSpecifications;


public class basicTest {
	private static final String BASE_URL = "https://petstore.swagger.io";
	private static final String BASE_PATH = "/v2";
	private static final RestAssuredConfig configWithTimeout = RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000).setParam(CoreConnectionPNames.SO_TIMEOUT, 1000));

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
		.spec(RequestSpecifications.postRequest)
		.body(requestBody,ObjectMapperType.GSON)
		.when()
		.post(EndPoints.ADD_PET)
		.then()
		.log()
		.body()
		.body("name", equalTo("Murzik"))
		.body("category.name", equalTo("animals"))
		.body("tags[1].name", equalTo("homeless"))
		.statusCode(200);
		given()
		.header("api_key","special_key")
		.pathParam("petId", petId)
		.when()
		.delete("/pet/{petId}")
		.then()
		.spec(ResponseSpecifications.statusCode200);
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

	@Test
	public void uploadImageTest() {
		File catPhoto = new File("src/main/java/resources/cat.png");
		given()
		.config(configWithTimeout)
		.pathParam("petId", 3)
		.formParam("additionalMetadata", "funny cat image")
		.multiPart(catPhoto)
		.when()
		.post("/pet/{petId}/uploadImage")
		.then()
		.time(lessThan(3L),TimeUnit.SECONDS)
		.spec(ResponseSpecifications.statusCode200);
	}

	public int getPetStatusCode() {
		int petId = 8989;
		return given()
				.pathParam("id",petId)
				.when()
				.get("/pet/{id}")
				.then()
				.extract()
				.statusCode();
	}
	@Test
	public void getPetAsync() {
		Awaitility.await().atMost(10, TimeUnit.SECONDS).until(()->this.getPetStatusCode() == 200);
	}
}