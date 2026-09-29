# ReservationHub API Automation

A Java-based REST API automation framework built using **REST Assured and TestNG** for validating the ReservationHub booking APIs.

The framework follows a layered, maintainable architecture with reusable service classes, POJO-based request and response models, authentication handling, JSON Schema validation, TestNG grouping, centralized request/response logging, and Allure reporting.

---

## Tech Stack

| Technology | Version | Purpose |
|---|---:|---|
| Java | 17+ | Programming language |
| REST Assured | 6.0.1 | REST API automation |
| TestNG | 7.12.0 | Test execution |
| Jackson | 2.22.3 | JSON serialization/deserialization |
| JSON Schema Validator | 6.0.1 | API contract validation |
| Allure | 2.29.1 | Test reporting |
| Maven | 3.x | Build and dependency management |

---

## API Under Test

The framework automates the public **Restful Booker API**.

**Base URL**

```text
https://restful-booker.herokuapp.com

```
***Allure Report***

https://nowitsabhi1727.github.io/ReservationHub/


### API Coverage

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/ping` | Health check |
| POST | `/auth` | Generate authentication token |
| GET | `/booking` | Retrieve booking IDs |
| GET | `/booking/{id}` | Retrieve booking by ID |
| POST | `/booking` | Create booking |
| PUT | `/booking/{id}` | Full booking update |
| PATCH | `/booking/{id}` | Partial booking update |
| DELETE | `/booking/{id}` | Delete booking |

---

## Framework Architecture

```text
                    ┌──────────────────────┐
                    │     Test Classes     │
                    │ Auth / Booking /     │
                    │ CRUD / Negative      │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │    Service Layer     │
                    │ AuthService          │
                    │ BookingService       │
                    │ PingService          │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │     BaseService      │
                    │ Request setup        │
                    │ HTTP operations      │
                    │ Authentication       │
                    │ Logging              │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │     REST Assured     │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   Restful Booker API │
                    └──────────────────────┘
```

### Design Approach

- **Test Layer** — Contains test scenarios and assertions.
- **Service Layer** — Encapsulates endpoint-specific API operations.
- **Base Layer** — Provides reusable REST Assured request infrastructure.
- **Model Layer** — Contains request and response POJOs.
- **Configuration Layer** — Loads environment-specific configuration.
- **Validation Layer** — Provides reusable response and JSON Schema validation.
- **Utility Layer** — Provides reusable authentication and test-data helpers.
- **Filter Layer** — Provides centralized request/response logging.
- **Listener Layer** — Provides TestNG execution logging.

---

## Project Structure

```text
ReservationHub
│
├── pom.xml
├── README.md
├── BUGS.md
├── .gitignore
│
└── src
    ├── main
    │   ├── java/com.api
    │   │   ├── base
    │   │   ├── config
    │   │   ├── constants
    │   │   ├── filters
    │   │   ├── models
    │   │   │   ├── request
    │   │   │   └── response
    │   │   └── services
    │   └── resources
    │       └── config.properties
    │
    └── test
        ├── java/com.api
        │   ├── listeners
        │   ├── tests
        │   └── utils
        └── resources
            ├── allure.properties
            ├── schemas
            └── testng-suites
```

---

## Test Coverage

### Authentication

- Valid credentials
- Invalid username
- Invalid password
- Empty credentials
- Authentication token generation
- Missing authentication for protected operations
- Invalid authentication for protected operations

### Booking Creation

- Valid booking creation
- Empty required fields
- Empty request payload
- Negative price
- Zero price
- Invalid booking date range
- Response body validation
- JSON Schema validation

### Booking Retrieval

- Retrieve booking IDs
- Retrieve booking by ID
- Filter bookings by firstname and lastname
- Filter bookings by check-in and checkout dates
- Non-existent booking ID
- Negative booking ID
- Response Schema validation

### Full Booking Update

- PUT booking update
- Authentication validation
- Updated response validation
- Persistence verification using GET

### Partial Booking Update

- PATCH booking update
- Update selected fields
- Verify unchanged fields
- Persistence verification
- Authentication validation

### Booking Deletion

- Delete booking
- Verify deleted booking
- Missing authentication
- Invalid authentication
- Already deleted booking behavior

### Health Check

- `/ping` endpoint validation

---

## Authentication

The framework obtains an authentication token dynamically through:

```text
POST /auth
```

Credentials are loaded through the configuration layer.

Authenticated booking operations use the API-supported Cookie mechanism:

```text
Cookie: token=<token>
```

Tokens are generated during test execution instead of being hardcoded into individual test cases.

---

## Test Data Management

The framework avoids depending on hardcoded booking IDs for scenarios that require controlled data.

Where required, tests create their own booking and capture the generated booking ID:

```text
Create Booking
      ↓
Capture Booking ID
      ↓
Perform API Operation
      ↓
Validate Response
      ↓
Verify Persisted State
```

This helps keep tests independent and reduces dependency on seeded records in the shared API environment.

---

## JSON Schema Validation

The framework validates response contracts using JSON Schema.

Current schemas:

```text
src/test/resources/schemas/

├── auth-response-schema.json
├── booking-ids-response-schema.json
├── booking-response-schema.json
└── create-booking-response-schema.json
```

Schema validation is used alongside field-level assertions to verify both response data and response structure.

---

## TestNG Groups

Tests are organized into:

```text
smoke
regression
negative
auth
```

Available TestNG suites:

```text
src/test/resources/testng-suites/

├── testng.xml
├── testng-smoke.xml
├── testng-regression.xml
├── testng-negative.xml
└── testng-auth.xml
```

---

## Prerequisites

Install:

- Java 17 or higher
- Maven 3.x
- Git

Verify:

```bash
java -version
mvn -version
git --version
```

---

## Configuration

Environment-specific configuration is maintained in:

```text
src/main/resources/config.properties
```

Example:

```properties
base.url=https://restful-booker.herokuapp.com
username=admin
password=password123
```

For production environments, credentials should be provided through environment variables or a secure secrets-management solution.

---

## Running the Tests

### Complete suite

```bash
mvn clean test
```

### Smoke tests

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-suites/testng-smoke.xml
```

### Regression tests

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-suites/testng-regression.xml
```

### Negative tests

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-suites/testng-negative.xml
```

### Authentication tests

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-suites/testng-auth.xml
```

---

## Reporting

The framework uses **Allure** for test reporting.

Generate the report:

```bash
mvn allure:report
```

Generated report:

```text
target/site/allure-maven-plugin/
```

### View Allure Report

Serve the report through a local HTTP server:

```bash
cd target/site/allure-maven-plugin
python3 -m http.server 8080
```

Open:

```text
http://localhost:8080
```

### Live Allure Report
The generated test report is published through GitHub Pages:
https://nowitsabhi1727.github.io/ReservationHub/

The generated test report is published through GitHub Pages:

The report provides:

- Overall execution summary
- Passed/failed tests
- Test grouping
- Request details
- Response details
- Assertions
- Failure information

A generated Allure report is also provided separately with the project submission.

---

## Request & Response Logging

A centralized REST Assured filter captures request and response information during test execution.

The logging includes:

- HTTP method
- Request URI
- Request headers
- Request body
- Response status
- Response headers
- Response body

This provides useful diagnostic information when investigating failed API tests.

---

## Defects Identified

During negative testing, the API was observed accepting invalid booking data.

Detailed defect reports are available in:

```text
BUGS.md
```

Confirmed scenarios include:

- Negative `totalprice` accepted
- Checkout date earlier than check-in accepted
- Empty `checkin` resulting in malformed date data

Additional observations and scenarios requiring further verification are also documented in `BUGS.md`.

---

## Environment Considerations

The Restful Booker API is a public shared sandbox.

The environment can:

- Reset booking data periodically
- Contain seeded records
- Experience cold starts
- Respond slowly
- Be affected by other users interacting with the same instance

The framework therefore creates controlled test data where required instead of relying heavily on pre-existing booking records.

---

## Known Limitations

- The API runs on a shared public sandbox.
- Test data can be reset by the environment.
- Response time can vary during cold starts.
- Some seeded records may not contain optional response fields.
- Date-filter behavior can be affected by the shared test environment and requires isolation before being treated as a confirmed API defect.

---

## Future Enhancements

Potential enhancements for a production-scale implementation include:

- CI/CD integration
- Environment-specific configuration
- Secure secret management
- Retry/wait handling for transient failures
- Data-driven and parameterized tests
- Parallel execution with stronger test-data isolation
- Performance testing
- OpenAPI-based contract validation
- Enhanced reporting with build and environment metadata
- Integration with test-management and defect-tracking tools

---

## Repository

**GitHub:**  
https://github.com/nowitsabhi1727/ReservationHub

---

## Author

### Abhishek Dudhani

Senior Software Engineer | QA Automation Engineer

**Automation & Testing**

- Java
- REST Assured
- Selenium WebDriver
- Playwright
- API Testing
- TestNG
- Maven
- Jenkins / CI-CD
- SQL
- Git / GitHub
- Postman
