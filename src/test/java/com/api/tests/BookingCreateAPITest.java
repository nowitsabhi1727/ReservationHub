package com.api.tests;

import com.api.listeners.TestNGListener;
import com.api.models.request.BookingDates;
import com.api.models.request.CreateBookingRequest;
import com.api.models.response.CreateBookingResponse;
import com.api.services.BookingService;
import com.api.validators.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


public class BookingCreateAPITest {

    @Test(description = "Verify user can create a booking",groups = {"smoke", "regression"})
    public void createBooking() {

        CreateBookingRequest createBookingRequest = new CreateBookingRequest.Builder()
                .firstname("Abhishek").lastname("Test").totalprice(1500).depositpaid(true).
                bookingdates(new BookingDates("2026-10-25", "2026-11-01"))
                .additionalneeds("Breakfast").build();

        BookingService bookingService= new BookingService();
        Response response=bookingService.createBooking(createBookingRequest);
        ResponseValidator.validateStatusCode(response,200);
        CreateBookingResponse createBookingResponse= response.as(CreateBookingResponse.class);
        //System.out.println(response.asPrettyString());
        //System.out.println(createBookingResponse.getBookingid());
        Assert.assertTrue(createBookingResponse.getBookingid() > 0);
        ResponseValidator.validateSchema(response,"schemas/create-booking-response-schema.json");


        Assert.assertEquals(createBookingResponse.getBooking().getFirstname(), createBookingRequest.getFirstname());
        Assert.assertEquals(createBookingResponse.getBooking().getLastname(), createBookingRequest.getLastname());
        Assert.assertEquals(createBookingResponse.getBooking().getTotalprice(), createBookingRequest.getTotalprice());
        Assert.assertEquals(createBookingResponse.getBooking().isDepositpaid(), createBookingRequest.isDepositpaid());
        Assert.assertEquals(createBookingResponse.getBooking().getAdditionalneeds(), createBookingRequest.getAdditionalneeds());
        Assert.assertEquals(createBookingResponse.getBooking().getBookingdates().getCheckin(), createBookingRequest.getBookingdates().getCheckin());
        Assert.assertEquals(createBookingResponse.getBooking().getBookingdates().getCheckout(), createBookingRequest.getBookingdates().getCheckout());
    }

}
