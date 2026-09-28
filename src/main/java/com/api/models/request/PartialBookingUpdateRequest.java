package com.api.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartialBookingUpdateRequest {
    private String firstname;
    private String lastname;
    private Integer totalprice;
    private Boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;


    public PartialBookingUpdateRequest() {
    }

    public PartialBookingUpdateRequest(String firstname,
                                       String lastname,
                                       Integer totalprice,
                                       Boolean depositpaid,
                                       BookingDates bookingdates,
                                       String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalneeds = additionalneeds;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Integer totalprice) {
        this.totalprice = totalprice;
    }

    public Boolean getDepositpaid() {
        return depositpaid;
    }

    public void setDepositpaid(Boolean depositpaid) {
        this.depositpaid = depositpaid;
    }

    public BookingDates getBookingdates() {
        return bookingdates;
    }

    public void setBookingdates(BookingDates bookingdates) {
        this.bookingdates = bookingdates;
    }

    public String getAdditionalneeds() {
        return additionalneeds;
    }

    public void setAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
    }

    //builder
    public static class Builder {

        private String firstname;
        private String lastname;
        private Integer totalprice;
        private Boolean depositpaid;
        private BookingDates bookingdates;
        private String additionalneeds;

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder totalprice(Integer totalprice) {
            this.totalprice = totalprice;
            return this;
        }

        public Builder depositpaid(Boolean depositpaid) {
            this.depositpaid = depositpaid;
            return this;
        }

        public Builder bookingdates(BookingDates bookingdates) {
            this.bookingdates = bookingdates;
            return this;
        }

        public Builder additionalneeds(String additionalneeds) {
            this.additionalneeds = additionalneeds;
            return this;
        }

        public PartialBookingUpdateRequest build() {

            PartialBookingUpdateRequest request =
                    new PartialBookingUpdateRequest();

            request.setFirstname(firstname);
            request.setLastname(lastname);
            request.setTotalprice(totalprice);
            request.setDepositpaid(depositpaid);
            request.setBookingdates(bookingdates);
            request.setAdditionalneeds(additionalneeds);

            return request;
        }
    }
}
