package com.api.models.response;

public class BookingIdResponse {
    private int bookingid;

    public BookingIdResponse() {
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    @Override
    public String toString() {
        return "{" + "bookingid=" + bookingid + '}';
    }
}
