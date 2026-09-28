package com.api.tests;

import com.api.models.request.BookingDates;
import com.api.models.request.CreateBookingRequest;
import com.api.models.request.PartialBookingUpdateRequest;
import com.api.models.response.Booking;
import com.api.models.response.CreateBookingResponse;
import com.api.services.BookingService;
import com.api.utils.TestDataHelper;
import com.api.validators.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingCRUDTest {

    @Test(description = "Verify complete booking lifecycle - Create, Read, PUT, PATCH and Delete",groups = {"smoke", "regression"})
    public void bookingCRUDLifecycle() {

        BookingService bookingService = new BookingService();

        // 1. Create booking
        CreateBookingRequest createRequest =
                new CreateBookingRequest.Builder()
                        .firstname("Abhishek")
                        .lastname("Test")
                        .totalprice(1500)
                        .depositpaid(true)
                        .bookingdates(new BookingDates("2026-10-25", "2026-11-01"))
                        .additionalneeds("Breakfast")
                        .build();

        Response createResponse = bookingService.createBooking(createRequest);

        // Validate Create response
        // Assert.assertEquals(createResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(createResponse,200);
        ResponseValidator.validateSchema(createResponse,"schemas/create-booking-response-schema.json");

        CreateBookingResponse createdBooking = createResponse.as(CreateBookingResponse.class);

        int bookingId = createdBooking.getBookingid();

        Assert.assertTrue(bookingId > 0);

        // Validate Create response body
        Assert.assertEquals(createdBooking.getBooking().getFirstname(), createRequest.getFirstname());
        Assert.assertEquals(createdBooking.getBooking().getLastname(), createRequest.getLastname());
        Assert.assertEquals(createdBooking.getBooking().getTotalprice(), createRequest.getTotalprice());
        Assert.assertEquals(createdBooking.getBooking().isDepositpaid(), createRequest.isDepositpaid());
        Assert.assertEquals(createdBooking.getBooking().getAdditionalneeds(), createRequest.getAdditionalneeds());
        Assert.assertEquals(createdBooking.getBooking().getBookingdates().getCheckin(), createRequest.getBookingdates().getCheckin());
        Assert.assertEquals(createdBooking.getBooking().getBookingdates().getCheckout(), createRequest.getBookingdates().getCheckout());


        // 2. Read booking
        Response getResponse = bookingService.getBookingById(bookingId);

        // Validate Read response
        ResponseValidator.validateStatusCode(getResponse,200);
        ResponseValidator.validateSchema(getResponse,"schemas/booking-response-schema.json");

        Booking booking = getResponse.as(Booking.class);

        // Validate Read response body
        Assert.assertEquals(booking.getFirstname(), createRequest.getFirstname());
        Assert.assertEquals(booking.getLastname(), createRequest.getLastname());
        Assert.assertEquals(booking.getTotalprice(), createRequest.getTotalprice());
        Assert.assertEquals(booking.isDepositpaid(), createRequest.isDepositpaid());
        Assert.assertEquals(booking.getAdditionalneeds(), createRequest.getAdditionalneeds());
        Assert.assertEquals(booking.getBookingdates().getCheckin(), createRequest.getBookingdates().getCheckin());
        Assert.assertEquals(booking.getBookingdates().getCheckout(), createRequest.getBookingdates().getCheckout());


        // 3. Get authentication token
        String token = TestDataHelper.getAuthToken();

        // Set authentication token
        bookingService.setAuthToken(token);


        // 4. Update booking using PUT
        CreateBookingRequest putRequest =
                new CreateBookingRequest.Builder()
                        .firstname("AbhishekPUT")
                        .lastname("TestPUT")
                        .totalprice(2500)
                        .depositpaid(false)
                        .bookingdates(new BookingDates("2026-11-10", "2026-11-20"))
                        .additionalneeds("Lunch")
                        .build();

        Response putResponse = bookingService.updateBooking(putRequest, bookingId);

        // Validate PUT response
        //Assert.assertEquals(putResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(putResponse,200);
        ResponseValidator.validateSchema(putResponse,"schemas/booking-response-schema.json");

        Booking putBooking = putResponse.as(Booking.class);

        // Validate PUT response body
        Assert.assertEquals(putBooking.getFirstname(), putRequest.getFirstname());
        Assert.assertEquals(putBooking.getLastname(), putRequest.getLastname());
        Assert.assertEquals(putBooking.getTotalprice(), putRequest.getTotalprice());
        Assert.assertEquals(putBooking.isDepositpaid(), putRequest.isDepositpaid());
        Assert.assertEquals(putBooking.getAdditionalneeds(), putRequest.getAdditionalneeds());
        Assert.assertEquals(putBooking.getBookingdates().getCheckin(), putRequest.getBookingdates().getCheckin());
        Assert.assertEquals(putBooking.getBookingdates().getCheckout(), putRequest.getBookingdates().getCheckout());


        // 5. Update booking using PATCH
        PartialBookingUpdateRequest patchRequest =
                new PartialBookingUpdateRequest.Builder()
                        .firstname("AbhishekPATCH")
                        .build();

        Response patchResponse = bookingService.partialUpdateBooking(patchRequest, bookingId);

        // Validate PATCH response
        //Assert.assertEquals(patchResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(patchResponse,200);
        ResponseValidator.validateSchema(patchResponse,"schemas/booking-response-schema.json");

        Booking patchBooking = patchResponse.as(Booking.class);

        // Validate PATCH response body
        Assert.assertEquals(patchBooking.getFirstname(), patchRequest.getFirstname());

        // Validate fields not included in PATCH remain unchanged
        Assert.assertEquals(patchBooking.getLastname(), putRequest.getLastname());
        Assert.assertEquals(patchBooking.getTotalprice(), putRequest.getTotalprice());
        Assert.assertEquals(patchBooking.isDepositpaid(), putRequest.isDepositpaid());
        Assert.assertEquals(patchBooking.getAdditionalneeds(), putRequest.getAdditionalneeds());
        Assert.assertEquals(patchBooking.getBookingdates().getCheckin(), putRequest.getBookingdates().getCheckin());
        Assert.assertEquals(patchBooking.getBookingdates().getCheckout(), putRequest.getBookingdates().getCheckout());


        // 6. Delete booking
        Response deleteResponse = bookingService.deleteBooking(bookingId);

        // Validate Delete response
        //Assert.assertEquals(deleteResponse.statusCode(), 201);
        ResponseValidator.validateStatusCode(deleteResponse,201);


        // 7. Verify booking is deleted
        Response getDeletedBookingResponse = bookingService.getBookingById(bookingId);

        // Assert.assertEquals(getDeletedBookingResponse.statusCode(), 404);
        ResponseValidator.validateStatusCode(getDeletedBookingResponse,404);

    }
}