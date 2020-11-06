package restAPITesting.restAssuredTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Category;
import models.Pet;
import models.Tags;


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
		// Data preparation
		int petId = 3;
		Category category = new Category();
		category.setId(0);
		category.setName("animals");
		String name = "Murzik";
		List<String> photoUrls = new ArrayList<String>();
		photoUrls.add("myMurzik.jpg");
		List<Tags> tags = new ArrayList<Tags>();
		Tags tag1 = new Tags();
		tag1.setId(0);
		tag1.setName("cat");
		Tags tag2 = new Tags();
		tag2.setId(1);
		tag2.setName("homeless");
		tags.add(tag1);
		tags.add(tag2);
		String status = "available";
		// request body filling
		Pet pet = new Pet();
		pet.setId(petId);
		pet.setCategory(category);
		pet.setName(name);
		pet.setPhotoUrls(photoUrls);
		pet.setStatus(status);
		pet.setTags(tags);
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(pet);
		System.out.println(json);

		given()
		.contentType("application/json")
		.body(pet)
		.when()
		.post("/pet")
		.then()
		.log()
		.body()
		.body("name", equalTo("Murzik"))
		.body("category.name", equalTo("animals"))
		.body("tags[1].name", equalTo("homeless"))
		.statusCode(200);

		//		given()
		//		.header("api_key","special-key")
		//		.pathParam("petId", petId+22222)
		//		.when()
		//		.delete("/pet/{petId}")
		//		.then()
		//		.log()
		//		.all()
		//		.statusCode(200);
	}

	@Ignore @Test
	public void userLogin(){
		Response res = given()
				.queryParam("username", "test")
				.queryParam("password", "test")
				.when()
				.get("/user/login")
				.then()
				.extract()
				.response();
		String header = res.getHeader("X-Expires-After");
		System.out.println(header);
	}

	@Test
	public void getPet(){
		int petId = 3;
		Response res = given()
				.pathParam("petId",petId)
				.when()
				.get("/pet/{petId}")
				.then()
				.time(lessThan(20L))
				.extract()
				.response();
		String jsonResponse = res.getBody().asString();
		Pet pet = new Gson().fromJson(jsonResponse, Pet.class);
		System.out.println(pet.getName());
	}
}
