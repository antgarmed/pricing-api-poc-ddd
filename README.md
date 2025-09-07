# Pricing API

[![Java](https://img.shields.io/badge/Java-21%2B-blue.svg)](https://adoptium.net)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Build-Maven-orange.svg)](https://maven.apache.org/)

A small Spring Boot service that exposes a **single read endpoint** to get the applicable price for a product of a brand at a given date. It focuses on clarity, tests, and a clean (hexagonal) design—keeping the solution intentionally minimal.

The service looks up prices stored in an in-memory H2 table `PRICES` with these columns:

* `BRAND_ID`, `PRODUCT_ID`
* `START_DATE`, `END_DATE` (validity window)
* `PRICE_LIST` (tariff identifier)
* `PRIORITY` (disambiguation when windows overlap)
* `PRICE`, `CURR`

**Endpoint**: `GET /prices?date=...&productId=...&brandId=...`
**Returns**: product, brand, the applied `priceList`, the effective date range, and final price (amount + currency).

The dataset included matches the exercise statement and is loaded via `schema.sql` + `data.sql`.

## Architecture

* **Hexagonal (Ports & Adapters)**

  * **Domain**: `Price` aggregate, `Money` value object.
  * **Application**: `GetPriceUseCase` orchestrates the query and selection.
  * **Ports**: `PriceRepository` (domain port).
  * **Adapters**: JPA adapter implements `PriceRepository`; REST controller exposes `/prices`.

* **Policy (business rule)**

  * The **highest `PRIORITY` wins** when multiple prices overlap.

* **Validation**

  * **Edge (REST)**: request shape (`@NotNull`, `@Positive`, and ISO date parsing).
  * **Domain**: invariants (e.g., `start < end`, `Money` non-negative).
  * **Application**: throws `PriceNotFoundException` (404) and `InvalidQueryException` (400).

* **Why no Flyway?**

  * The exercise asks to avoid over-engineering. H2 with `schema.sql`/`data.sql` is enough.

## Tech Stack

* Java 21
* Spring Boot 3.5
* Spring Data JPA (H2 in-memory)
* JUnit 5, Mockito, RestAssured
* Springdoc OpenAPI (Swagger UI)
* Lombok

## Getting Started

### Prerequisites

* JDK 21+
* Maven 3.9+ (or use the provided Maven Wrapper `./mvnw`)

### Run the application

```bash
./mvnw spring-boot:run
```

App will start at `http://localhost:8080`.

### API Docs (Swagger UI)

* OpenAPI JSON: `http://localhost:8080/api-docs`
* Swagger UI: serves the static `openapi.yaml` (configured at `springdoc.swagger-ui.url=docs/openapi.yaml`), typically available at:

  * `http://localhost:8080/swagger-ui/index.html`

> If you move/rename the spec file, update `springdoc` properties accordingly.

## API

### `GET /prices`

**Query params**

* `date` (ISO-8601 LocalDateTime, e.g., `2020-06-14T16:00:00`)
* `productId` (positive long)
* `brandId` (positive long)

**Response (200)**

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "money": {
    "price": 25.45,
    "currency": "EUR"
  }
}
```

**Errors (Problem Details, RFC 7807 format)**

* `400 Bad Request` – invalid/missing parameters, or `InvalidQueryException`.
* `404 Not Found` – `PriceNotFoundException` (no applicable price).

Example:

```json
{
  "status": 404,
  "title": "Price not found",
  "detail": "Price not found",
  "instance": "/prices"
}
```

## How to Test

### Run all tests

```bash
./mvnw test
```

### What is covered

* **Unit**

  * Domain invariants (`Money`, `Price` factory).
  * Application orchestration (`GetPriceUseCase`).
  * Selector policy (priority + tie-breaks).
* **Infrastructure**

  * JPA adapter over H2 (`schema.sql` + `data.sql`) with the shared dataset.
* **E2E (RestAssured)**

  * The 5 queries from the statement:

    * 2020-06-14 10:00 → priceList **1**
    * 2020-06-14 16:00 → priceList **2**
    * 2020-06-14 21:00 → priceList **1**
    * 2020-06-15 10:00 → priceList **3**
    * 2020-06-16 21:00 → priceList **4**

  * Error handling examples:

    * 404 when no price exists for a date (e.g., `2019-01-01T10:00:00`)
    * 400 when parameters are invalid (e.g., negative ids, bad date format)

## Quick manual checks

A ready-to-use HTTP file is included:

* `requests.http` – five happy-path requests + one 404 error case.

Open it in IntelliJ (native .http support) or VS Code (REST Client extension) and hit “Send Request”.

## Docker

You can run the service as a container.

**Dockerfile (runtime-only) pattern**

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENV JAVA_TOOL_OPTIONS="-Duser.timezone=Europe/Madrid"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

Build and run:

```bash
docker compose up --build
```

## Design Notes

* **Keep it minimal**: no Flyway, no query bus to fit the exercise scope.
* **Business rules live in code** (domain/application), not in SQL.
* **Problem Details**: consistent error responses (400/404) with a compact JSON shape.

## Project Layout (simplified)

```
src
 ├─ main
 │   ├─ java/com/example/pricing
 │   │   ├─ domain/ (Price, Money, exceptions)
 │   │   ├─ application/ (GetPriceUseCase, command, exceptions)
 │   │   ├─ infrastructure/
 │   │   │   ├─ api/ (GetPriceController, GlobalExceptionHandler)
 │   │   │   └─ persistence/ (PriceEntity, JpaPriceRepository, Adapter)
 │   └─ resources/
 │       ├─ application.properties
 │       ├─ schema.sql
 │       ├─ data.sql
 │       └─ static/docs/openapi.yaml
 └─ test
     ├─ unit & application tests
     ├─ infra JPA tests (@DataJpaTest)
     └─ e2e RestAssured tests (@SpringBootTest)
```
