package com.api.tests;

import com.api.services.BookingService;
import com.api.utils.TestDataHelper;
import com.api.validators.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingDeleteTest {

    @Test(description = "Verify user can delete an existing booking",groups = {"regression"})
    public void deleteBooking() {

        BookingService bookingService = new BookingService();

        // Create test booking
        int bookingId = TestDataHelper.createBooking(bookingService);

        // Get authentication token
        String token = TestDataHelper.getAuthToken();

        // Set authentication token
        bookingService.setAuthToken(token);

        // DELETE /booking/{id}
        Response deleteResponse =
                bookingService.deleteBooking(bookingId);

        // Validate DELETE response
        //Assert.assertEquals(deleteResponse.statusCode(), 201);
        ResponseValidator.validateStatusCode(deleteResponse,201);

        // Verify booking is deleted
        Response getResponse =
                bookingService.getBookingById(bookingId);

        //Assert.assertEquals(getResponse.statusCode(), 404);
        ResponseValidator.validateStatusCode(getResponse,404);

    }


    @Test(description = "Verify DELETE request is rejected when authentication is missing",groups = {"negative", "auth"})
    public void deleteBookingWithoutAuth() {

        BookingService bookingService = new BookingService();

        // Create test booking
        int bookingId = TestDataHelper.createBooking(bookingService);

        // DELETE /booking/{id} without authentication
        Response deleteResponse =
                bookingService.deleteBooking(bookingId);

        // Validate response
        //Assert.assertEquals(deleteResponse.statusCode(), 403);
        ResponseValidator.validateStatusCode(deleteResponse,403);
    }


    @Test(description = "Verify DELETE request is rejected when authentication is invalid",groups = {"negative", "auth"})
    public void deleteBookingWithInvalidAuth() {

        BookingService bookingService = new BookingService();

        // Create test booking
        int bookingId = TestDataHelper.createBooking(bookingService);

        // Set invalid authentication token
        bookingService.setAuthToken("invalidToken12345");

        // DELETE /booking/{id} with invalid authentication
        Response deleteResponse =
                bookingService.deleteBooking(bookingId);

        // Validate response
       // Assert.assertEquals(deleteResponse.statusCode(), 403);
        ResponseValidator.validateStatusCode(deleteResponse,403);

    }


    @Test(description = "Verify deleting an already deleted booking",groups = {"negative", "regression"})
    public void deleteBookingAgain() {

        BookingService bookingService = new BookingService();

        // Create test booking
        int bookingId = TestDataHelper.createBooking(bookingService);

        // Get authentication token
        String token = TestDataHelper.getAuthToken();

        // Set authentication token
        bookingService.setAuthToken(token);

        // Delete booking first time
        Response firstDeleteResponse =
                bookingService.deleteBooking(bookingId);

        //Assert.assertEquals(firstDeleteResponse.statusCode(), 201);
        ResponseValidator.validateStatusCode(firstDeleteResponse,201);


        // Verify booking no longer exists
        Response getResponse =
                bookingService.getBookingById(bookingId);

        //Assert.assertEquals(getResponse.statusCode(), 404);
        ResponseValidator.validateStatusCode(getResponse,404);


        // Delete the same booking again
        Response secondDeleteResponse =
                bookingService.deleteBooking(bookingId);

        // Validate observed behavior
        //Assert.assertEquals(secondDeleteResponse.statusCode(), 405);
        ResponseValidator.validateStatusCode(secondDeleteResponse,405);

        Assert.assertEquals(secondDeleteResponse.contentType(), "text/plain; charset=utf-8");
    }
}