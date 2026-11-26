# Java Test Automation Practice

A Java project where I practise test automation step by step, one topic per branch and pull request.

Lessons 1–6 test a small customs domain (inspired by ATLAS, the German customs system): validators for tariff numbers and country codes, a duty calculator, a repository with SQL queries and an HTTP client for a tariff service. Lesson 7 adds Playwright browser tests against [saucedemo.com](https://www.saucedemo.com), a public demo shop.

## Tech stack

Java 17 · Maven · JUnit 5 · Mockito · H2 (SQL) · Cucumber / Gherkin · WireMock · Playwright for Java

## Running the tests

Requirements: JDK 17 or newer, Maven.

```bash
mvn test                                  # all tests
mvn test -Dtest='de.practice.zoll.**'     # domain tests (lessons 1–6)
mvn test -Dtest='de.practice.ui.**'       # browser tests (lesson 7)
mvn test -Dtest='de.practice.ui.**' -Dheadless=false   # watch the browser
```

The first Playwright run downloads Chromium automatically.

## Project structure

```
src/main/java/de/practice/zoll/     customs domain code under test
src/test/java/de/practice/zoll/     unit, SQL, WireMock and Cucumber tests
src/test/java/de/practice/ui/       Playwright tests
src/test/java/de/practice/ui/pages/ Page Objects
src/test/resources/                 Gherkin feature files, SQL schema and test data
```

## Lessons

### 1. JUnit 5 and boundary values
`WarennummerValidatorTest`: boundary tests for the 8-digit tariff number (REQ-01). The tests found a bug in the validator; I documented it like a bug report (steps, expected and actual result, severity, priority) before fixing it.

### 2. Parameterized tests
`WarennummerValidatorTest`, `LaendercodeValidatorTest`: equivalence classes as tables with `@ParameterizedTest`, `@ValueSource` and `@CsvSource`, including null and empty input.

### 3. Mockito
`AbgabenRechnerTest`: the duty calculator depends on a `TarifService`, which is mocked. Covers the normal case, rounding of half cents, the €150 duty-free limit, negative values and an unreachable tariff service.

### 4. Test data and SQL
`ZollanmeldungRepositoryTest`: queries run against an in-memory H2 database, set up from `schema.sql` and `testdata.sql` before each test. Checks which customs declarations end up on the inspection list.

### 5. Cucumber
`abgaben.feature` with `AbgabenSteps`: the duty calculation requirements written as Given / When / Then scenarios.

### 6. WireMock
`HttpTarifServiceTest`: the HTTP client is tested against a simulated tariff API. Covers a successful response, request headers and parameters, server errors (500, 502, 503, 599) and a slow response that exceeds the timeout.

### 7. Playwright with Page Objects
Browser tests against saucedemo.com.

- `BaseUiTest`: one browser per test class, a fresh browser context per test, so tests don't share cookies or cart state
- `pages/`: `LoginPage`, `InventoryPage`, `CartPage`. They hold selectors and actions only; assertions stay in the tests
- Selectors use `data-test` attributes, which don't change when texts or styling change
- `LoginTest`: login parameterized over several users, the locked-out user's error message, and the same login on a desktop (1280×720) and a mobile (390×844) viewport
- `InventoryTest`: cart badge after adding two products, the added product shows up in the cart, removing the last product hides the badge

## Next steps

- Sorting tests on the inventory page (`sortBy` is already in `InventoryPage`)
- Checkout flow with a `CheckoutPage`
- GitHub Actions workflow that runs the tests on every pull request and uploads the Playwright trace when a test fails
- `CLAUDE.md` with rules for coding agents (selector strategy, where assertions go)