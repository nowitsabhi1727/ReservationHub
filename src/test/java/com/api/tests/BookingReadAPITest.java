package com.api.tests;

import com.api.models.request.BookingDates;
import com.api.models.request.CreateBookingRequest;
import com.api.models.response.Booking;
import com.api.models.response.BookingIdResponse;
import com.api.models.response.CreateBookingResponse;
import com.api.services.BookingService;
import com.api.validators.ResponseValidator;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class BookingReadAPITest {
	
	
	@Test(description="Verify booking IDs are returned",groups = {"smoke", "regression"})
	public void getBookingIds() {

        // GET /booking
        BookingService bookingService = new BookingService();
        Response response=bookingService.getBookingIds();
        List<BookingIdResponse> bookingIds =
                response.as(new TypeRef<List<BookingIdResponse>>() {});

        ResponseValidator.validateStatusCode(response,200);
        ResponseValidator.validateSchema(response,"schemas/booking-ids-response-schema.json");
        Assert.assertFalse(bookingIds.isEmpty());

        for (BookingIdResponse bookingId : bookingIds) {
             //System.out.println(bookingId);
            //System.out.println(bookingId.getBookingid());
            Assert.assertTrue(bookingId.getBookingid() > 0);
        }
	}

    @Test(description = "Verify user can retrieve booking by ID",groups = {"regression"})
    public void getBookingById() {
        // GET /booking/{id}
        BookingService bookingService = new BookingService();
        Response bookingIdsResponse = bookingService.getBookingIds();
        //Assert.assertEquals(bookingIdsResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(bookingIdsResponse,200);
        List<BookingIdResponse> bookingIds = bookingIdsResponse.as(new TypeRef<List<BookingIdResponse>>() {});
        Assert.assertFalse(bookingIds.isEmpty());
        int bookingId = bookingIds.get(0).getBookingid();

        Response response=bookingService.getBookingById(bookingId);
        //Assert.assertEquals(response.statusCode(), 200);
        ResponseValidator.validateStatusCode(response,200);
        ResponseValidator.validateSchema(response, "schemas/booking-response-schema.json");

        Booking booking= response.as(Booking.class);

        Assert.assertNotNull(booking.getFirstname());
        Assert.assertNotNull(booking.getLastname());
        Assert.assertTrue(booking.getTotalprice() >= 0);
    }

    @Test(description = "Verify bookings can be filtered by firstname and lastname",groups = {"regression"})
    public void getBookingIdsByName() {

        CreateBookingRequest createRequest =
                new CreateBookingRequest.Builder()
                        .firstname("FilterFirstName")
                        .lastname("FilterLastName")
                        .totalprice(1000)
                        .depositpaid(true)
                        .bookingdates(new BookingDates("2026-10-25", "2026-11-01"))
                        .additionalneeds("Breakfast")
                        .build();

        BookingService bookingService = new BookingService();
        Response createResponse = bookingService.createBooking(createRequest);

        //Assert.assertEquals(createResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(createResponse,200);

        CreateBookingResponse createdBooking = createResponse.as(CreateBookingResponse.class);

        int bookingId = createdBooking.getBookingid();
        Assert.assertTrue(bookingId > 0);
        Response response = bookingService.getBookingIdsByName(createRequest.getFirstname(), createRequest.getLastname());
        //Assert.assertEquals(response.statusCode(), 200);
        ResponseValidator.validateStatusCode(response,200);
        ResponseValidator.validateSchema(response, "schemas/booking-ids-response-schema.json");


        List<BookingIdResponse> bookingIds =
                response.as(new TypeRef<List<BookingIdResponse>>() {});

        Assert.assertFalse(bookingIds.isEmpty());

        boolean bookingFound = false;

        for (BookingIdResponse booking : bookingIds) {
            if (booking.getBookingid() == bookingId) {
                bookingFound = true;
                break;
            }
        }

        Assert.assertTrue(bookingFound);
    }

    @Test(description = "Verify bookings can be filtered by checkin and checkout",groups = {"regression"})
    public void getBookingIdsByDate() {

        CreateBookingRequest createRequest =
                new CreateBookingRequest.Builder()
                        .firstname("DateFilterFirstName")
                        .lastname("DateFilterLastName")
                        .totalprice(1000)
                        .depositpaid(true)
                        .bookingdates(new BookingDates("2026-10-25", "2026-11-01"))
                        .additionalneeds("Breakfast")
                        .build();

        BookingService bookingService = new BookingService();
        Response createResponse = bookingService.createBooking(createRequest);

        //Assert.assertEquals(createResponse.statusCode(), 200);
        ResponseValidator.validateStatusCode(createResponse,200);


        CreateBookingResponse createdBooking = createResponse.as(CreateBookingResponse.class);

        int bookingId = createdBooking.getBookingid();
        Assert.assertTrue(bookingId > 0);

        Response response =
                bookingService.getBookingIdsByDate(createRequest.getBookingdates().getCheckin(), createRequest.getBookingdates().getCheckout());

        //Assert.assertEquals(response.statusCode(), 200);
        ResponseValidator.validateStatusCode(response,200);
        ResponseValidator.validateSchema(response, "schemas/booking-ids-response-schema.json");

        List<BookingIdResponse> bookingIds =
                response.as(new TypeRef<List<BookingIdResponse>>() {});

        Assert.assertFalse(bookingIds.isEmpty());
        boolean bookingFound = false;
        for (BookingIdResponse booking : bookingIds) {
            if (booking.getBookingid() == bookingId) {
                bookingFound = true;
                break;
            }
        }
        Assert.assertTrue(bookingFound);
        ResponseValidator.validateSchema(response,"schemas/booking-ids-response-schema.json");

    }


    @Test(description = "Verify retrieving a non-existent booking returns 404",groups = {"negative", "regression"})
    public void getNonExistentBooking() {

        int bookingId = 999999;

        BookingService bookingService=new BookingService();
        Response response =
                bookingService.getBookingById(bookingId);

        //Assert.assertEquals(response.statusCode(), 404);
        ResponseValidator.validateStatusCode(response,404);
    }
    @Test(description = "Verify retrieving a booking with negative ID is rejected",groups = {"negative", "regression"})
    public void getBookingWithNegativeId() {

        int bookingId = -1;
        BookingService bookingService=new BookingService();
        Response response =
                bookingService.getBookingById(bookingId);

        //Assert.assertEquals(response.statusCode(), 404);
        ResponseValidator.validateStatusCode(response,404);

    }

}
