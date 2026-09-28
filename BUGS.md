# API Bugs Report

## Overview

This document records reproducible functional/validation issues identified while testing the Restful Booker API as part of the ReservationHub API automation assignment.

The bugs below were identified through automated API tests using REST Assured.

---

## BUG-001 — Negative `totalprice` is accepted when creating a booking

**Severity:** Medium

**Endpoint:** `POST /booking`

### Description

The API accepts a booking with a negative `totalprice` value instead of rejecting the request with a validation error.

### cURL

```bash
curl --request POST \
  --url https://restful-booker.herokuapp.com/booking \
  --header 'Content-Type: application/json' \
  --data '{
    "firstname": "Abhishek",
    "lastname": "Test",
    "totalprice": -100,
    "depositpaid": true,
    "bookingdates": {
      "checkin": "2026-10-25",
      "checkout": "2026-11-01"
    },
    "additionalneeds": "Breakfast"
  }'
```

### Expected Result

The API should reject a negative booking price with a client validation response, such as:

- HTTP `400 Bad Request`, or
- another documented validation response.

The booking should not be created.

### Actual Result

The API accepted the request and returned:

- HTTP `200 OK`
- A booking ID
- A booking containing the negative `totalprice`.

### Impact

Invalid financial data can be persisted as a valid booking, which could lead to incorrect pricing or downstream calculation issues.

### Automated Test

`BookingCreateNegativeTest.negativeTotalPrice()`

---

## BUG-002 — Checkout date earlier than check-in date is accepted

**Severity:** Medium

**Endpoint:** `POST /booking`

### Description

The API accepts a booking where the checkout date occurs before the check-in date.

### cURL

```bash
curl --request POST \
  --url https://restful-booker.herokuapp.com/booking \
  --header 'Content-Type: application/json' \
  --data '{
    "firstname": "Abhishek",
    "lastname": "Test",
    "totalprice": 1500,
    "depositpaid": true,
    "bookingdates": {
      "checkin": "2026-11-01",
      "checkout": "2026-10-25"
    },
    "additionalneeds": "Breakfast"
  }'
```

### Expected Result

The API should validate the booking date range and reject the request because:

```text
checkout < checkin
```

An appropriate client validation response such as HTTP `400 Bad Request` would be expected.

### Actual Result

The API accepted the request and returned:

- HTTP `200 OK`
- A booking ID
- The invalid date range in the created booking.

### Impact

Invalid date ranges can result in logically impossible reservations and may cause incorrect availability, duration, pricing, or downstream booking-processing behavior.

### Automated Test

`BookingCreateNegativeTest.checkoutBeforeCheckin()`

---

## BUG-003 — Empty check-in date is accepted and converted to an invalid date value

**Severity:** High

**Endpoint:** `POST /booking`

### Description

The API accepts an empty `checkin` date instead of rejecting the request. The returned booking contains the malformed value `0NaN-aN-aN`.

### cURL

```bash
curl --request POST \
  --url https://restful-booker.herokuapp.com/booking \
  --header 'Content-Type: application/json' \
  --data '{
    "firstname": "Abhishek",
    "lastname": "Test",
    "totalprice": 1500,
    "depositpaid": true,
    "bookingdates": {
      "checkin": "",
      "checkout": "2026-11-01"
    },
    "additionalneeds": "Breakfast"
  }'
```

### Expected Result

The API should reject an empty `checkin` date with a validation error, such as HTTP `400 Bad Request`.

The API should never persist or return a malformed date value.

### Actual Result

The API accepted the request with HTTP `200 OK` and returned a booking containing:

```json
"bookingdates": {
  "checkin": "0NaN-aN-aN",
  "checkout": "2026-11-01"
}
```

### Impact

The API accepts invalid input and transforms it into malformed date data. This can cause failures or inconsistent behavior for consumers that expect `checkin` to follow the documented date format.

### Automated Test

`BookingCreateNegativeTest.emptyCheckinDate()`

---


## Additional Observed Issues — Summary Only

The following additional scenarios were observed during negative API testing. They are listed here as a summary only and are not included as fully documented bugs above because some behaviors were inconsistent across executions or require further validation.

| Scenario | Observed Behavior | Status |
|---|---|---|
| Empty `firstname` | Booking was accepted in one execution; behavior was inconsistent in a later execution | Requires re-verification |
| Empty `lastname` | Booking was created successfully despite the field being empty | Observed |
| Empty `checkin` | Booking was accepted and generated malformed date value `0NaN-aN-aN` | Covered as BUG-003 |
| Negative `totalprice` | Booking was created with a negative price | Covered as BUG-001 |
| Zero `totalprice` | Booking was accepted with `totalprice = 0` | Observed |
| Checkout before check-in | Booking was created with an invalid date range | Covered as BUG-002 |
| Empty request payload `{}` | API returned HTTP `500` instead of a client-side validation response | Observed |
| Non-existent booking ID | GET request returned HTTP `404` | Expected/handled negative scenario |
| Negative booking ID | GET request returned HTTP `404` | Expected/handled negative scenario |
| Missing authentication for update | PUT/PATCH/DELETE requests were rejected with HTTP `403` | Expected/handled negative scenario |
| Invalid authentication for update | PUT/PATCH/DELETE requests were rejected with HTTP `403` | Expected/handled negative scenario |
| Delete already-deleted booking | Subsequent DELETE returned HTTP `405` with `text/plain` response | Observed; contract should be verified |
| Date-based booking filter | Exact date filter returned an empty result despite a controlled booking being created | Requires further investigation before classifying as a defect |

> **Note:** The summary intentionally distinguishes confirmed/repeated observations from scenarios that require re-verification. This avoids reporting environment-dependent or inconsistent behavior as a confirmed product defect.

## Notes

- These findings are based on observed behavior during automated execution against the public Restful Booker sandbox.
- The sandbox is a shared/public environment and may reset its data periodically.
- Test results should therefore be reproducible against the same API behavior but may vary if the sandbox implementation or seeded data changes.
- The booking filter-by-date test was not included as a confirmed defect in this document because the observed behavior requires further distinction between an API filtering defect and the characteristics of the shared/reset sandbox environment.

## Summary

| Bug ID | Issue | Severity | Endpoint |
|---|---|---|---|
| BUG-001 | Negative `totalprice` accepted | Medium | `POST /booking` |
| BUG-002 | Checkout before check-in accepted | Medium | `POST /booking` |
| BUG-003 | Empty check-in produces malformed date | High | `POST /booking` |
