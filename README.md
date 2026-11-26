# Zoll testing practice

Small Java project for practising test automation.
Topic: customs data, like ATLAS.

## Setup (once)

1. Install IntelliJ IDEA Community (free). It can download a JDK for you (version 17 or newer).
2. Open this folder in IntelliJ: File > Open > select the folder `zoll-testing-practice`.
   IntelliJ recognises `pom.xml` as a Maven project and downloads JUnit by itself.
3. Open `src/test/java/.../WarennummerValidatorTest.java` and click the green arrow next to the class name.
   The example test should pass.

## Lesson 1: JUnit 5 and boundary values

Requirement REQ-01 is in `WarennummerValidator.java`.

1. Write the tests marked TODO 1 to 6. Every test is a method with `@Test`
   that calls `validator.isValid(...)` and checks the result with `assertTrue` or `assertFalse`.
2. Run all tests. At least one will fail. That means you found a bug.
3. Write down the bug like a bug report: steps, expected result, actual result, severity, priority.
4. Only then fix the bug in `WarennummerValidator.java` and run the tests again until everything is green.

## Coming next

- Lesson 2: `@ParameterizedTest`, one test with many inputs (equivalence classes in a table)
- Lesson 3: Mockito, testing a class that depends on another service
- Lesson 4: test data and SQL
- Lesson 5: Cucumber, the same requirement written as Given / When / Then
- Lesson 6: Selenide, a UI test in the browser

## Lesson 7: Playwright browser tests with Page Objects

Target: https://www.saucedemo.com (public demo shop with `data-test` attributes).

The first run downloads the browsers automatically (takes a minute).

Run only the UI tests:
mvn test -Dtest='de.practice.ui.**'
Watch the browser:
mvn test -Dtest='de.practice.ui.**' -Dheadless=false

Structure:
- `BaseUiTest`: browser setup and teardown, new context per test
- `pages/`: Page Objects (selectors and actions, no assertions)
- `*Test`: scenarios and assertions

Next steps:
1. CartPage and CheckoutPage, full checkout test
2. @ParameterizedTest over users
3. Mobile viewport variant
4. GitHub Actions workflow, upload the Playwright trace on failure
5. CLAUDE.md with rules for coding agents (selector strategy, where assertions go)

