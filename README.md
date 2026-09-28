# ReservationHub — API Automation Framework

## Overview

ReservationHub is a REST API automation framework built for the QA Engineer (API Automation) assignment using **Java, REST Assured, TestNG, Jackson, JSON Schema validation and Allure**.

The suite targets the public **Restful Booker** sandbox and focuses on meaningful API coverage, negative testing, authentication, response contracts, maintainable framework structure, and actionable reporting.

The primary goal is not maximum test count, but a reliable suite that can detect realistic booking defects and provide enough evidence for a developer to investigate a failure.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| REST Assured 6.0.1 | API automation |
| TestNG 7.12.0 | Test execution and grouping |
| Jackson 2.22.3 | JSON serialization/deserialization |
| JSON Schema Validator 6.0.1 | Response contract validation |
| Allure 2.29.1 | Test reporting |
| Maven | Build and dependency management |

---

## API Under Test

**Base URL**

```text
https://restful-booker.herokuapp.com
```

### Endpoints covered

| Method | Endpoint | Coverage |
|---|---|---|
| GET | `/ping` | Health check |
| POST | `/auth` | Authentication/token generation |
| GET | `/booking` | Booking IDs and filters |
| GET | `/booking/{id}` | Retrieve booking |
| POST | `/booking` | Create booking |
| PUT | `/booking/{id}` | Full booking update |
| PATCH | `/booking/{id}` | Partial booking update |
| DELETE | `/booking/{id}` | Delete booking |

---

## Framework Architecture

```text
src
├── main
│   ├── java/com.api
│   │   ├── base
│   │   │   └── BaseService.java
│   │   ├── config
│   │   │   └── ConfigReader.java
│   │   ├── constants
│   │   │   └── Endpoints.java
│   │   ├── filters
│   │   │   └── RequestResponseLoggingFilter.java
│   │   ├── models
│   │   │   ├── request
│   │   │   └── response
│   │   └── services
│   │       ├── AuthService.java
│   │       ├── BookingService.java
│   │       └── PingService.java
│   └── resources
│       └── config.properties
│
└── test
    ├── java/com.api
    │   ├── tests
    │   ├── utils
    │   ├── validators
    │   └── listeners
    └── resources
        ├── schemas
        ├── testng-suites
        └── allure.properties
```

### Design approach

The framework follows a service-layer approach:

```text
Test Classes
     ↓
Service Classes
     ↓
BaseService
     ↓
REST Assured
     ↓
Restful Booker API
```

- **Test classes** contain test scenarios and assertions.
- **Service classes** encapsulate endpoint/API operations.
- **BaseService** contains reusable HTTP request infrastructure.
- **Models** represent request and response payloads.
- **ConfigReader** centralizes environment configuration.
- **TestDataHelper** provides reusable test data/setup operations.
- **ResponseValidator** centralizes reusable response validation.
- **JSON schemas** validate important response contracts.
- **TestNG listeners and Allure** provide execution/reporting support.

---

## Test Strategy

The suite uses a risk-based approach rather than testing every possible input combination.

### 1. Happy-path coverage

The core booking lifecycle is covered:

```text
Create → Read → PUT → PATCH → Delete → Verify deletion
```

Assertions validate both status codes and response content rather than relying only on HTTP status.

### 2. Authentication

The suite covers:

- Successful token generation
- Invalid username
- Invalid password
- Empty credentials
- Missing authentication for write operations
- Invalid authentication for write operations

The framework uses the API's token-based Cookie authentication:

```text
Cookie: token=<token>
```

### 3. Negative and boundary testing

The suite probes scenarios including:

- Negative price
- Zero price
- Empty required fields
- Empty payload
- Invalid booking date ranges
- Invalid/malformed date input
- Non-existent booking IDs
- Negative booking IDs
- Missing authentication
- Invalid authentication

The purpose is to verify both expected validation behavior and defects in the system under test.

### 4. Response contract testing

JSON Schema validation is applied to important responses, including:

- Authentication response
- Booking ID list
- Booking response
- Create-booking response

This helps detect structural/API contract regressions that field-level assertions alone may miss.

---

## Test Organization

Tests are grouped using TestNG groups:

```text
smoke
regression
negative
auth
```

Suite files are available under:

```text
src/test/resources/testng-suites/
```

Available suites:

```text
testng.xml
testng-smoke.xml
testng-regression.xml
testng-negative.xml
testng-auth.xml
```

---

## How to Run

### Run the complete suite

```bash
mvn clean test
```

### Run Smoke tests

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-suites/testng-smoke.xml
```

### Run Regression tests

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-suites/testng-regression.xml
```

### Run Negative tests

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-suites/testng-negative.xml
```

### Run Authentication tests

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-suites/testng-auth.xml
```

---

## Configuration

Environment-specific values are separated from test code in:

```text
src/main/resources/config.properties
```

Current configuration:

```properties
base.url=https://restful-booker.herokuapp.com
username=admin
password=password123
```

`ConfigReader` loads these values at runtime so endpoint configuration and credentials are not hardcoded throughout the test classes.

For a real production framework, credentials should be supplied through environment variables or a secrets-management solution rather than committed to source control.

---

## Reporting

The framework uses **Allure** for structured test reporting.

Generate the report with:

```bash
mvn allure:report
```

The generated report is located at:

```text
target/site/allure-maven-plugin/
```

The submitted `ReservationHub-Allure-Report.zip` contains the generated report output.

The report provides:

- Overall pass/fail summary
- Individual test results
- Test grouping
- Request details
- Response details
- Assertions and failure information
- Execution evidence

Because Allure is a browser-based JavaScript application, the extracted report should be served through a local HTTP server rather than opened directly using `file://` if the browser displays a blank/loading page.

Example:

```bash
cd target/site/allure-maven-plugin
python3 -m http.server 8080
```

Then open:

```text
http://localhost:8080
```

---

## API Defects Found

The detailed defect report is available in:

```text
BUGS.md
```

The confirmed defects include:

1. Negative `totalprice` is accepted when creating a booking.
2. Checkout date earlier than check-in date is accepted.
3. Empty `checkin` can be accepted and returned as malformed date data (`0NaN-aN-aN`).

Additional observed scenarios are also summarized in `BUGS.md`, with inconsistent or environment-dependent observations clearly separated from confirmed defects.

---

## Shared Sandbox and Test Reliability

Restful Booker is a public shared sandbox. The assignment notes that:

- The instance contains seeded records.
- Data resets periodically.
- The environment can experience cold starts and slow responses.
- Tests must not depend on data created by another test.

To address this:

- Tests create their own booking data where test-specific data is required.
- Created booking IDs are captured dynamically rather than relying on hardcoded IDs.
- Authentication tokens are generated during test execution.
- CRUD tests use dynamically created resources.
- Tests are designed to be independently executable wherever practical.
- The framework does not rely on execution order for core booking scenarios.

The public/shared nature of the environment means that occasional external instability or reset behavior can still affect tests.

---

## Deliberate Scope / What Is Not Covered

The suite focuses on the assignment's core API testing requirements.

The following were deliberately not expanded into a large test matrix:

- Exhaustive combinations of every field and data type
- Large-scale performance/load testing
- Full security penetration testing
- Production-scale concurrency testing
- UI/browser automation
- Full CI/CD pipeline configuration

These areas could be added for a production system, but they were outside the primary objective of this assignment.

---

## Known Limitations

1. **Shared test environment**  
   The API is public and can be affected by resets or other users.

2. **Cold starts / response latency**  
   The sandbox may occasionally respond slowly after inactivity.

3. **Schema variability**  
   Some existing seeded booking responses may omit optional fields such as `additionalneeds`; the response schema reflects the observed API contract rather than assuming every seeded record has every optional property.

4. **Environment-dependent filtering behavior**  
   Date-based filtering was observed to return an unexpected empty result for a controlled booking. This has been kept separate from the confirmed defects until the behavior can be isolated from shared-environment/reset effects.

5. **Credentials**  
   The assignment uses the public sandbox credentials. A production implementation should obtain credentials from secure environment configuration or secret management.

---

## Future Improvements

If this framework were being developed for a production service, potential next steps would include:

- CI/CD integration
- Environment-specific configuration
- Secure secret management
- Retry/wait handling for transient cold-start failures
- Data-driven/parameterized negative tests
- Parallel execution with stronger test-data isolation
- Performance smoke testing
- API contract validation from an OpenAPI specification
- Enhanced reporting with environment/build metadata

---
