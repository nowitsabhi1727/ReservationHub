package com.api.tests;

import com.api.models.request.BookingDates;
import com.api.models.request.CreateBookingRequest;
import com.api.models.response.Booking;
import com.api.services.BookingService;
import com.api.utils.TestDataHelper;
import com.api.validators.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingUpdateTest {

    @Test(description = "Verify user can update an existing booking", groups = {"regression"})
    public void updateBooking() {

        BookingService bookingService = new BookingService();

        // Create test booking
        int bookingId = TestDataHelper.createBooking(bookingService);

        // Get authentication token
        String token = TestDataHelper.getAuthToken();

        // Set authentication token
        bookingService.setAuthToken(token);

        // Create updated booking data
        CreateBookingRequest updateRequest =
                new CreateBookingRequest.Builder()
                        .firstname("AbhishekUpdated")
                        .lastname("TestUpdated")
                        .totalprice(2500)
                        .depositpaid(false)
                        .bookingdates(
                                new BookingDates(
                                        "2026-11-10",
                                        "2026-11-20"
                                )
                        )
                        .additionalneeds("Lunch")
                        .build();

        // PUT /booking/{id}
        Response updateResponse =
                bookingService.updateBooking(updateRequest, bookingId);

        // Validate PUT response
        //Assert.assertEquals(updateResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(updateResponse, 200);

        Booking updatedBooking =
                updateResponse.as(Booking.class);

        Assert.assertEquals(
                updatedBooking.getFirstname(),
                updateRequest.getFirstname()
        );

        Assert.assertEquals(
                updatedBooking.getLastname(),
                updateRequest.getLastname()
        );

        Assert.assertEquals(
                updatedBooking.getTotalprice(),
                updateRequest.getTotalprice()
        );

        Assert.assertEquals(
                updatedBooking.isDepositpaid(),
                updateRequest.isDepositpaid()
        );

        Assert.assertEquals(
                updatedBooking.getAdditionalneeds(),
                updateRequest.getAdditionalneeds()
        );

        Assert.assertEquals(
                updatedBooking.getBookingdates().getCheckin(),
                updateRequest.getBookingdates().getCheckin()
        );

        Assert.assertEquals(
                updatedBooking.getBookingdates().getCheckout(),
                updateRequest.getBookingdates().getCheckout()
        );

        // GET booking again and verify persistence
        Response getResponse =
                bookingService.getBookingById(bookingId);

//        Assert.assertEquals(getResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(getResponse, 200);
        ResponseValidator.validateSchema(getResponse, "schemas/booking-response-schema.json");


        Booking bookingAfterUpdate =
                getResponse.as(Booking.class);

        Assert.assertEquals(
                bookingAfterUpdate.getFirstname(),
                updateRequest.getFirstname()
        );

        Assert.assertEquals(
                bookingAfterUpdate.getLastname(),
                updateRequest.getLastname()
        );

        Assert.assertEquals(
                bookingAfterUpdate.getTotalprice(),
                updateRequest.getTotalprice()
        );

        Assert.assertEquals(
                bookingAfterUpdate.isDepositpaid(),
                updateRequest.isDepositpaid()
        );

        Assert.assertEquals(
                bookingAfterUpdate.getAdditionalneeds(),
                updateRequest.getAdditionalneeds()
        );

        Assert.assertEquals(
                bookingAfterUpdate.getBookingdates().getCheckin(),
                updateRequest.getBookingdates().getCheckin()
        );

        Assert.assertEquals(
                bookingAfterUpdate.getBookingdates().getCheckout(),
                updateRequest.getBookingdates().getCheckout()
        );
    }


    @Test(description = "Verify PUT request is rejected when authentication is missing", groups = {"negative", "auth"})
    public void updateBookingWithoutAuth() {

        BookingService bookingService = new BookingService();

        // Create test booking
        int bookingId = TestDataHelper.createBooking(bookingService);

        // Create updated booking data
        CreateBookingRequest updateRequest =
                new CreateBookingRequest.Builder()
                        .firstname("AbhishekUpdated")
                        .lastname("TestUpdated")
                        .totalprice(2500)
                        .depositpaid(false)
                        .bookingdates(
                                new BookingDates(
                                        "2026-11-10",
                                        "2026-11-20"
                                )
                        )
                        .additionalneeds("Lunch")
                        .build();

        // PUT /booking/{id} without authentication
        Response updateResponse =
                bookingService.updateBooking(updateRequest, bookingId);

        // Validate response
        //Assert.assertEquals(updateResponse.statusCode(), 403);
        ResponseValidator.validateStatusCode(updateResponse, 403);
    }


    @Test(description = "Verify PUT request is rejected when authentication is invalid", groups = {"negative", "auth"})
    public void updateBookingWithInvalidAuth() {

        BookingService bookingService = new BookingService();

        // Create test booking
        int bookingId = TestDataHelper.createBooking(bookingService);

        // Set invalid authentication token
        bookingService.setAuthToken("invalidToken12345");

        // Create updated booking data
        CreateBookingRequest updateRequest =
                new CreateBookingRequest.Builder()
                        .firstname("AbhishekUpdated")
                        .lastname("TestUpdated")
                        .totalprice(2500)
                        .depositpaid(false)
                        .bookingdates(
                                new BookingDates(
                                        "2026-11-10",
                                        "2026-11-20"
                                )
                        )
                        .additionalneeds("Lunch")
                        .build();

        // PUT /booking/{id} with invalid authentication
        Response updateResponse =
                bookingService.updateBooking(updateRequest, bookingId);

        // Validate response
//        Assert.assertEquals(updateResponse.statusCode(), 403);
        ResponseValidator.validateStatusCode(updateResponse, 403);
    }
}