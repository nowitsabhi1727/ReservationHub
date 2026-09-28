package com.api.tests;

import com.api.services.PingService;
import com.api.validators.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PingAPITest {

    @Test(description = "Verify API health check",groups = {"smoke"})
    public void ping() {

        PingService pingService = new PingService();
        Response response = pingService.healthCheck();
        ResponseValidator.validateStatusCode(response, 201);
        Assert.assertEquals(response.asString(), "Created");
    }
}
