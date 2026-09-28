package com.api.utils;

import com.api.config.ConfigReader;
import com.api.models.request.AuthRequest;
import com.api.models.request.BookingDates;
import com.api.models.request.CreateBookingRequest;
import com.api.models.response.AuthResponse;
import com.api.models.response.CreateBookingResponse;
import com.api.services.AuthService;
import com.api.services.BookingService;
import io.restassured.response.Response;
import org.testng.Assert;

public class TestDataHelper {
    public static String getAuthToken() {

        AuthService authService = new AuthService();

        Response authResponse =
                authService.login(
                        new AuthRequest(ConfigReader.get("username"), ConfigReader.get("password"))
                );

        Assert.assertEquals(authResponse.statusCode(), 200);

        AuthResponse auth =
                authResponse.as(AuthResponse.class);

        String token = auth.getToken();

        Assert.assertNotNull(token);
        Assert.assertFalse(token.isEmpty());

        return token;
    }


    public static int createBooking(BookingService bookingService) {

        CreateBookingRequest createRequest =
                new CreateBookingRequest.Builder()
                        .firstname("Abhishek")
                        .lastname("Test")
                        .totalprice(1500)
                        .depositpaid(true)
                        .bookingdates(
                                new BookingDates(
                                        "2026-10-25",
                                        "2026-11-01"
                                )
                        )
                        .additionalneeds("Breakfast")
                        .build();

        Response createResponse =
                bookingService.createBooking(createRequest);

        Assert.assertEquals(createResponse.statusCode(), 200);

        CreateBookingResponse createdBooking =
                createResponse.as(CreateBookingResponse.class);

        int bookingId = createdBooking.getBookingid();

        Assert.assertTrue(bookingId > 0);

        return bookingId;
    }
}
