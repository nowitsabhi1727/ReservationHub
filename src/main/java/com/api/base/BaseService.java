package com.api.base;

import com.api.config.ConfigReader;
import com.api.filters.RequestResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseService {

	private final RequestSpecification requestSpecification;
	public BaseService() {

        requestSpecification= given().baseUri(ConfigReader.get("base.url"))
                .filter(new RequestResponseLoggingFilter());;
	}
	
	protected Response postRequest(Object payload, String endpoint) {
		return requestSpecification.contentType(ContentType.JSON).body(payload).post(endpoint);
	}

    protected Response getRequest(String endpoint) {
        // GET
        return requestSpecification.contentType(ContentType.JSON).get(endpoint);
    }

    protected Response getRequest(String endpoint,int pathParam) {
        // GET
        return requestSpecification.contentType(ContentType.JSON)
                .pathParam("id", pathParam)
                .get(endpoint);
    }

    protected Response getRequest(String endpoint, String param1, String value1, String param2, String value2)
    {
        return requestSpecification
                .queryParam(param1, value1)
                .queryParam(param2, value2)
                .get(endpoint);
    }

    public void setAuthToken(String token){
        requestSpecification.header("Cookie","token=" + token);
    }

    protected Response putRequest(Object payload, String endpoint,int pathParam){
      return requestSpecification.contentType(ContentType.JSON).header("Accept","application/json")
              .pathParam("id", pathParam)
                .body(payload).put(endpoint);
    }

    protected Response patchRequest(Object payload, String endpoint, int pathParam) {
        return requestSpecification
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .pathParam("id", pathParam)
                .body(payload)
                .patch(endpoint);
    }

    protected Response deleteRequest(String endpoint, int pathParam) {
        return requestSpecification
                .contentType(ContentType.JSON)
                .pathParam("id", pathParam)
                .delete(endpoint);
    }

}
