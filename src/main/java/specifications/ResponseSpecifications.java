package specifications;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.ResponseSpecification;

public class ResponseSpecifications {
	public static ResponseSpecification statusCode200 = new ResponseSpecBuilder()
			.expectStatusCode(200)
			.log(LogDetail.ALL)
			.build();

}
