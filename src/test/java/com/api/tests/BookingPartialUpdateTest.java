package com.api.tests;

import com.api.models.request.PartialBookingUpdateRequest;
import com.api.models.response.Booking;
import com.api.services.BookingService;
import com.api.utils.TestDataHelper;
import com.api.validators.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingPartialUpdateTest {

    @Test(description = "Verify user can partially update an existing booking",groups = {"regression"})
    public void partialUpdateBooking() {

        BookingService bookingService = new BookingService();

        // 1. Create test booking
        int bookingId = TestDataHelper.createBooking(bookingService);

        // 2. Get authentication token
        String token = TestDataHelper.getAuthToken();

        // 3. Set authentication token
        bookingService.setAuthToken(token);

        // 4. Get booking before update
        Response getBeforeUpdateResponse =
                bookingService.getBookingById(bookingId);

        //Assert.assertEquals(getBeforeUpdateResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(getBeforeUpdateResponse,200);

        Booking bookingBeforeUpdate =
                getBeforeUpdateResponse.as(Booking.class);

        // 5. Create partial update request
        PartialBookingUpdateRequest updateRequest =
                new PartialBookingUpdateRequest.Builder()
                        .firstname("AbhishekUpdated")
                        .build();

        // 6. PATCH /booking/{id}
        Response patchResponse =
                bookingService.partialUpdateBooking(updateRequest, bookingId);

        // 7. Validate PATCH response
        //Assert.assertEquals(patchResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(patchResponse,200);


        Booking updatedBooking =
                patchResponse.as(Booking.class);

        // 8. Verify updated field
        Assert.assertEquals(
                updatedBooking.getFirstname(),
                updateRequest.getFirstname()
        );

        // 9. Verify other fields remain unchanged
        Assert.assertEquals(
                updatedBooking.getLastname(),
                bookingBeforeUpdate.getLastname()
        );

        Assert.assertEquals(
                updatedBooking.getTotalprice(),
                bookingBeforeUpdate.getTotalprice()
        );

        Assert.assertEquals(
                updatedBooking.isDepositpaid(),
                bookingBeforeUpdate.isDepositpaid()
        );

        Assert.assertEquals(
                updatedBooking.getAdditionalneeds(),
                bookingBeforeUpdate.getAdditionalneeds()
        );

        Assert.assertEquals(
                updatedBooking.getBookingdates().getCheckin(),
                bookingBeforeUpdate.getBookingdates().getCheckin()
        );

        Assert.assertEquals(
                updatedBooking.getBookingdates().getCheckout(),
                bookingBeforeUpdate.getBookingdates().getCheckout()
        );

        // 10. Verify persistence
        Response getAfterUpdateResponse =
                bookingService.getBookingById(bookingId);

        //Assert.assertEquals(getAfterUpdateResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(getAfterUpdateResponse,200);
        ResponseValidator.validateSchema(getAfterUpdateResponse,"schemas/booking-response-schema.json");

        Booking bookingAfterUpdate =
                getAfterUpdateResponse.as(Booking.class);

        Assert.assertEquals(
                bookingAfterUpdate.getFirstname(),
                updateRequest.getFirstname());

        Assert.assertEquals(
                bookingAfterUpdate.getLastname(),
                bookingBeforeUpdate.getLastname());

        Assert.assertEquals(
                bookingAfterUpdate.getTotalprice(),
                bookingBeforeUpdate.getTotalprice());

        Assert.assertEquals(
                bookingAfterUpdate.isDepositpaid(),
                bookingBeforeUpdate.isDepositpaid());

        Assert.assertEquals(
                bookingAfterUpdate.getAdditionalneeds(),
                bookingBeforeUpdate.getAdditionalneeds());

        Assert.assertEquals(
                bookingAfterUpdate.getBookingdates().getCheckin(),
                bookingBeforeUpdate.getBookingdates().getCheckin());

        Assert.assertEquals(
                bookingAfterUpdate.getBookingdates().getCheckout(),
                bookingBeforeUpdate.getBookingdates().getCheckout());

    }


    @Test(description = "Verify PATCH request is rejected when authentication is missing",groups = {"negative", "auth"})
    public void partialUpdateBookingWithoutAuth() {

        BookingService bookingService = new BookingService();

        // Create test booking
        int bookingId = TestDataHelper.createBooking(bookingService);

        // Create partial update request
        PartialBookingUpdateRequest updateRequest =
                new PartialBookingUpdateRequest.Builder()
                        .firstname("AbhishekUpdated")
                        .build();

        // PATCH without authentication
        Response patchResponse =
                bookingService.partialUpdateBooking(updateRequest, bookingId);

        // Validate response
        //Assert.assertEquals(patchResponse.statusCode(), 403);
        ResponseValidator.validateStatusCode(patchResponse,403);

    }


    @Test(description = "Verify PATCH request is rejected when authentication is invalid",groups = {"negative", "auth"})
    public void partialUpdateBookingWithInvalidAuth() {

        BookingService bookingService = new BookingService();

        // Create test booking
        int bookingId = TestDataHelper.createBooking(bookingService);

        // Set invalid authentication token
        bookingService.setAuthToken("invalid-token-12345");

        // Create partial update request
        PartialBookingUpdateRequest updateRequest =
                new PartialBookingUpdateRequest.Builder()
                        .firstname("AbhishekUpdated")
                        .build();

        // PATCH with invalid authentication
        Response patchResponse =
                bookingService.partialUpdateBooking(updateRequest, bookingId);

        // Validate response
        //Assert.assertEquals(patchResponse.statusCode(), 403);
        ResponseValidator.validateStatusCode(patchResponse,403);

    }
}