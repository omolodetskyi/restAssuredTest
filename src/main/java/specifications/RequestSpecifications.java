package specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

public class RequestSpecifications {
	public static RequestSpecification postRequest = new RequestSpecBuilder()
			.setContentType("application/json")
			.log(LogDetail.ALL)
			.build();

}
