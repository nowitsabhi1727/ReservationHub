package com.api.tests;

import com.api.config.ConfigReader;
import com.api.models.request.AuthRequest;
import com.api.models.response.AuthErrorResponse;
import com.api.models.response.AuthResponse;
import com.api.services.AuthService;
import com.api.validators.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AuthAPITest {

    @Test(description = "Verify successful authentication",groups = {"smoke", "auth"})
    public void validLogin() {

        AuthRequest authRequest = new AuthRequest(ConfigReader.get("username"), ConfigReader.get("password"));
        AuthService authService = new AuthService();
        Response response = authService.login(authRequest);
        ResponseValidator.validateStatusCode(response,200);
        AuthResponse authResponse = response.as(AuthResponse.class);
        //System.out.println(authResponse.getToken());
        Assert.assertNotNull(authResponse.getToken());
        Assert.assertFalse(authResponse.getToken().isEmpty());
    }

    @Test(description = "Verify authentication fails with invalid username",groups = {"negative", "auth"})
    public void invalidUsername() {
        // invalid username
        AuthRequest request = new AuthRequest(ConfigReader.get("wrong.username"), ConfigReader.get("password"));
        AuthService authService = new AuthService();
        Response response = authService.login(request);
        ResponseValidator.validateStatusCode(response,200);
        AuthErrorResponse errorResponse = response.as(AuthErrorResponse.class);
        Assert.assertEquals(errorResponse.getReason(),"Bad credentials");
    }

    @Test(description = "Verify authentication fails with invalid password",groups = {"negative", "auth"})
    public void invalidPassword() {
        // invalid password
        AuthRequest request = new AuthRequest(ConfigReader.get("username"), ConfigReader.get("wrong.password"));
        AuthService authService = new AuthService();
        Response response = authService.login(request);
        ResponseValidator.validateStatusCode(response,200);
        AuthErrorResponse errorResponse = response.as(AuthErrorResponse.class);
        Assert.assertEquals(errorResponse.getReason(),"Bad credentials");
    }

    @Test(description = "Verify authentication fails with empty credentials",groups = {"negative", "auth"})
    public void emptyCredentials() {
        // empty username/password
        AuthRequest request = new AuthRequest("", "");
        AuthService authService = new AuthService();
        Response response = authService.login(request);
        ResponseValidator.validateStatusCode(response,200);
        AuthErrorResponse errorResponse = response.as(AuthErrorResponse.class);
        Assert.assertEquals(errorResponse.getReason(),"Bad credentials");
    }

    @Test(description = "Verify authentication response contract", groups = {"regression", "auth"})
    public void responseContract() {
        // token exists, correct response structure
        //Will add the code in later part
        AuthRequest authRequest = new AuthRequest(ConfigReader.get("username"), ConfigReader.get("password"));
        AuthService authService = new AuthService();
        Response response = authService.login(authRequest);
        //ResponseValidator.validateStatusCode(response,200);
        ResponseValidator.validateStatusCode(response,200);
        AuthResponse authResponse = response.as(AuthResponse.class);

        Assert.assertNotNull(authResponse.getToken());
        Assert.assertFalse(authResponse.getToken().isEmpty());
        //response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/auth-response-schema.json"));
        ResponseValidator.validateSchema(response,"schemas/auth-response-schema.json");
    }
}
