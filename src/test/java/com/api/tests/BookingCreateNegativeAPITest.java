package com.api.tests;

import com.api.models.request.BookingDates;
import com.api.models.request.CreateBookingRequest;
import com.api.services.BookingService;
import com.api.validators.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingCreateNegativeAPITest {

    private final BookingService bookingService = new BookingService();

    @Test(description = "Verify booking creation with empty firstname",groups = {"negative"})
    public void emptyFirstname() {

        CreateBookingRequest request =
                new CreateBookingRequest.Builder()
                        .firstname("")
                        .lastname("Test")
                        .totalprice(111)
                        .depositpaid(true)
                        .bookingdates(new BookingDates("2026-10-25", "2026-11-02"))
                        .additionalneeds("Breakfast")
                        .build();

        Response response = bookingService.createBooking(request);

        ResponseValidator.validateStatusCode(response,200);
    }

    @Test(description = "Verify booking creation with empty lastname",groups = {"negative"})
    public void emptyLastname() {

        CreateBookingRequest request =
                new CreateBookingRequest.Builder()
                        .firstname("Abhishek")
                        .lastname("")
                        .totalprice(111)
                        .depositpaid(true)
                        .bookingdates(new BookingDates("2026-10-25", "2026-11-02"))
                        .additionalneeds("Breakfast")
                        .build();

        Response response = bookingService.createBooking(request);

        ResponseValidator.validateStatusCode(response,200);
    }

    @Test(description = "Verify booking creation with empty checkin date",groups = {"negative"})
    public void emptyCheckinDate() {

        CreateBookingRequest request =
                new CreateBookingRequest.Builder()
                        .firstname("Abhishek")
                        .lastname("Test")
                        .totalprice(111)
                        .depositpaid(true)
                        .bookingdates(new BookingDates("", "2026-11-02"))
                        .additionalneeds("Breakfast")
                        .build();

        Response response = bookingService.createBooking(request);

        ResponseValidator.validateStatusCode(response,200);
    }

    @Test(description = "Verify booking creation with empty payload",groups = {"negative"})
    public void emptyPayload() {

//        CreateBookingRequest request =
//                new CreateBookingRequest.Builder().build();
        String payload = "{}";

        Response response = bookingService.createBooking(payload);

        Assert.assertEquals(response.statusCode(), 500);
    }

    @Test(description = "Verify booking creation with negative total price",groups = {"negative"})
    public void negativeTotalPrice() {

        CreateBookingRequest request =
                new CreateBookingRequest.Builder()
                        .firstname("Abhishek")
                        .lastname("Test")
                        .totalprice(-111)
                        .depositpaid(true)
                        .bookingdates(new BookingDates("2026-10-02", "2026-11-02"))
                        .additionalneeds("Breakfast")
                        .build();

        Response response = bookingService.createBooking(request);

        ResponseValidator.validateStatusCode(response,200);
    }

    @Test(description = "Verify booking creation with zero total price",groups = {"negative"})
    public void zeroTotalPrice() {

        CreateBookingRequest request =
                new CreateBookingRequest.Builder()
                        .firstname("Abhishek")
                        .lastname("Test")
                        .totalprice(0)
                        .depositpaid(true)
                        .bookingdates(new BookingDates("2026-10-02", "2026-11-02"))
                        .additionalneeds("Breakfast")
                        .build();

        Response response = bookingService.createBooking(request);

        ResponseValidator.validateStatusCode(response,200);
    }

    @Test(description = "Verify booking creation with checkout date before checkin date",groups = {"negative"})
    public void checkoutBeforeCheckin() {

        CreateBookingRequest request =
                new CreateBookingRequest.Builder()
                        .firstname("Abhishek")
                        .lastname("Test")
                        .totalprice(100)
                        .depositpaid(true)
                        .bookingdates(new BookingDates("2027-10-02", "2026-11-02"))
                        .additionalneeds("Breakfast")
                        .build();

        Response response = bookingService.createBooking(request);

        ResponseValidator.validateStatusCode(response,200);
    }
}