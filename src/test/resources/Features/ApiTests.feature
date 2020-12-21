@Test
Feature: API Tests

  Scenario: 1. Is Rates API for Latest Foreign Exchange rates available
    Given Rates API for Latest Foreign Exchange rates
    When The API is available
    Then An automated test suite should run which will assert the success status of the response


  Scenario Outline: 2. Is Rates API for Latest Foreign Exchange rates for "<base>" base and "<symbol>" symbol available
    Given Rates API for Latest Foreign Exchange rates
    When The API is available
    Then An automated test suite should run which will assert the response for "<base>" base and "<symbol>" symbol

    Examples:
      | base    | symbol                              |
      | EUR     |                                     |
      | EUR     | PLN                                 |
      | PLN     | GBP                                 |
      | PLN     | EUR,GBP                             |
      | EUR     | HKD,CHF,PLN,CAD,NZD,JPY,RUB,USD,AUD |
      |         |                                     |
      |         | EUR                                 |
      |         | USD                                 |
      | PLN     |                                     |
      | PLN,USD |                                     |


  Scenario Outline: 3. Call Rates API for Latest Foreign Exchange rates with <url> URL
    Given Rates API for Latest Foreign Exchange rates is called with "<url>" URL
    When The API is available
    Then Test case should assert the correct response supplied by the call for "<url>"

    Examples:
      | url                                   |
      | https://api.ratesapi.io/              |
      | https://api.ratesapi.io/api/          |
      | https://api.ratesapi.io/ap1/          |
      | https://api.ratesapi.io/api/blablabla |


  Scenario Outline: 4. Is Foreign Exchange rates API available for <date> date
    Given Rates API for Specific date Foreign Exchange rates with "<date>" date
    When The API is available
    Then An automated test suite should run which will assert the success status of the response

    Examples:
      | date       |
      | 2010-01-12 |
      | 2020-12-12 |
      | 2021-12-12 |
      | 1988-02-27 |


  Scenario Outline: 5. Is Foreign Exchange rates API for <date> date, <base> base and <symbol> symbol available
    Given Rates API for Specific date Foreign Exchange rates with "<date>" date
    When The API is available
    Then An automated test suite should run which will assert the response for "<date>" date, "<base>" base and "<symbol>" symbol

    Examples:
      | date       | base | symbol                                      |
      | 2010-01-12 | PLN  | EUR,GBP                                     |
      | 2020-12-12 | EUR  | HKD,CHF,CZK,CNY,RUB,PLN,CAD,JPY,RUB,USD,AUD |
      | 2022-01-12 | PLN  | EUR,GBP                                     |
