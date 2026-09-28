package com.api.services;

import com.api.base.BaseService;
import com.api.constants.Endpoints;
import com.api.models.request.CreateBookingRequest;
import com.api.models.request.PartialBookingUpdateRequest;
import io.restassured.response.Response;

public class BookingService extends BaseService {


    public Response createBooking(CreateBookingRequest payload){
        return postRequest(payload, Endpoints.BOOKING);
    }
    public Response createBooking(String payload) {
        return postRequest(payload, Endpoints.BOOKING);
    }

    public Response getBookingIds(){
        return getRequest(Endpoints.BOOKING);
    }

    public Response getBookingById(int bookingId){
        return getRequest(Endpoints.BOOKING_BY_ID,bookingId);
    }

    public Response getBookingIdsByName(String firstname, String lastname) {
        return getRequest(Endpoints.BOOKING, "firstname", firstname, "lastname", lastname);
    }

    public Response getBookingIdsByDate(String checkin, String checkout) {
        return getRequest(Endpoints.BOOKING, "checkin", checkin, "checkout", checkout);
    }

    public Response updateBooking(CreateBookingRequest payload,int bookingId){
        return putRequest(payload,Endpoints.BOOKING_BY_ID,bookingId);
    }

    public Response partialUpdateBooking(PartialBookingUpdateRequest payload, int bookingId) {
        return patchRequest(payload, Endpoints.BOOKING_BY_ID, bookingId);
    }

    public Response deleteBooking(int bookingId) {
        return deleteRequest(Endpoints.BOOKING_BY_ID, bookingId);
    }

}
