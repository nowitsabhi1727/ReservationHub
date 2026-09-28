package com.api.validators;

import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.AssertJUnit.assertEquals;

public class ResponseValidator {

    private ResponseValidator() {
    }

    public static void validateStatusCode(Response response, int expectedStatusCode) {
        assertEquals(response.statusCode(), expectedStatusCode);
    }


    public static void validateSchema(Response response, String schemaPath) {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(schemaPath));
    }
}
