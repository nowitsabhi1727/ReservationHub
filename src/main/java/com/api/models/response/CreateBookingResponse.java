package com.api.models.response;

public class CreateBookingResponse {

    /*
    {
    "bookingid": 1731,
    "booking": {
        "firstname": "ABC",
        "lastname": "Brown",
        "totalprice": 111,
        "depositpaid": true,
        "bookingdates": {
            "checkin": "2020-01-01",
            "checkout": "2019-01-01"
        },
        "additionalneeds": "Breakfast"
    }
}
     */

    private int bookingid;
    private Booking booking;

    public CreateBookingResponse() {
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public String toString() {
        return "CreateBookingResponse{" +
                "bookingid=" + bookingid +
                ", booking=" + booking +
                '}';
    }




}
