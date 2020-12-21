package rest;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static io.restassured.RestAssured.given;
import static stepdefs.RatesApiStepDefs.API_DEFAULT_URI;

public class ApiValidator {

    ApiConnector apiConnector = new ApiConnector();
    RatesAPIResponse ratesAPIResponse = new RatesAPIResponse();
    public static Response response;

    /**
     * Checks message type
     */
    public void validateResponseType() {
        Assert.assertEquals("Response message type incorrect.", "application/json",
                apiConnector.callApiWithGivenURI().getContentType());
    }

    /**
     * Returns response code
     *
     * @return response code
     */
    private int getResponseCode() {
        return response.getStatusCode();
    }

    /**
     * Returns response message
     *
     * @returnresponse message
     */
    private String getResponseMessage() {
        return response.jsonPath().get().toString();
    }

    public void validateResponse(String dateString, String base, String symbol) {
        RequestSpecification httpRequest = given();
        response = httpRequest.get("?base=" + base + "&symbols=" + symbol);
        System.out.println("Rates API called with URL : " + SpecificationQuerier.query(httpRequest).getURI());
        validateStatusCode(getResponseCode(), getResponseMessage(), false);
        System.out.println("Response is: " + response.asString());
        getRatesResponse(response.asString());
        System.out.print("Validating request currencies... ");
        validateCurrency(getRequestCurrencies(base, symbol));
        System.out.print("Validating response currencies... ");
        validateCurrency(ratesAPIResponse.getCurrencies());
        validateDate(dateString, "?base=" + base + "&symbols=" + symbol);

    }

    /**
     * Validates response status codes
     * Test would fail for
     *
     * @param statusCode
     * @param message
     * @param isNegative
     */
    private void validateStatusCode(int statusCode, String message, Boolean isNegative) {
        System.out.println("Validating HTTP response message...");

        Assert.assertNotEquals("HTTP/1.1 401 Unauthorized: The user is unauthorized to access the requested resource", 401, statusCode);
        Assert.assertNotEquals("HTTP/1.1 403 Forbidden: The requested resource is unavailable at this present time", 403, statusCode);
        Assert.assertNotEquals("HTTP/1.1 404 Not Found: The requested resource could not be found", 404, statusCode);
        Assert.assertNotEquals("HTTP/1.1 405 Method Not Allowed: The request method is not supported by the following resource", 405, statusCode);
        Assert.assertNotEquals("HTTP/1.1 406 Not Acceptable: The request was not acceptable", 406, statusCode);
        Assert.assertNotEquals("HTTP/1.1 408 Request Timeout: The server timed out waiting for the request", 408, statusCode);
        if (statusCode == 400) {
            if (!isNegative) {
                Assert.assertNotEquals("HTTP/1.1 400 " + message, 400, statusCode);
            } else {
                System.out.println("NEGATIVE TEST\nTEST PASSED - Error message is: HTTP/1.1 " + statusCode + ": " + message);
            }
        } else if (isNegative)
            System.out.println("NEGATIVE TEST\nTEST PASSED - Error message is: HTTP/1.1 " + statusCode + ": " + message);
    }

    /**
     * Checks if currency matches Java available currencies
     *
     * @param currency
     */
    public void validateCurrency(List<String> currency) {
        for (int i = 0; i < currency.size(); i++) {
            Assert.assertTrue("Currency " + currency + " does not exist", java.util.Currency.getAvailableCurrencies().toString().contains(currency.get(i)));
        }
        System.out.println("[OK]");
    }


    private void validateDate(String dateString, String params) {
        System.out.println("Validating dates...");
        RequestSpecification httpRequest = given();
        LocalDate currentDate = LocalDate.now();
        LocalDate responseDate = LocalDate.parse(ratesAPIResponse.getDate());

        if (!dateString.equals("latest")) {
            LocalDate requestDateLocal = LocalDate.parse(dateString);
            if (requestDateLocal.isAfter(currentDate)) {
                System.out.println("Future date found in request - " + dateString + "\nVerifying if result match response for current date...");
                apiConnector.buildURI(API_DEFAULT_URI, "latest");
                Assert.assertEquals("Requests with future dates should match current date responses.",
                        response.asString(),
                        httpRequest.get(params).asString());
                System.out.println("TEST PASSED - Future data response match current date responses");
            } else {
                Assert.assertEquals("Requested date doesn't match with date from response", requestDateLocal, responseDate);
            }
        } else {
            Assert.assertEquals("Requested date doesn't match with date from response", currentDate, responseDate);

        }
    }

    /**
     * Returns requested currencies
     *
     * @param base
     * @param symbol
     * @return
     */
    private List<String> getRequestCurrencies(String base, String symbol) {
        List<String> currencies = new ArrayList<>(Arrays.asList(base.split(",")));
        currencies.addAll(Arrays.asList(symbol.split(",")));
        return currencies;
    }


    void getRatesResponse(String ratesResponseJSON) {
        ratesAPIResponse = apiConnector.getRatesResponseObject(ratesResponseJSON);
    }

    /**
     * Calls API for url provided by user and runs validation of status codes
     */
    public void validateCodesForUrl() {
        RequestSpecification httpRequest = given();
        validateResponseType();
        ratesAPIResponse = apiConnector.getRatesResponseObject(httpRequest.get().asString());
        response = httpRequest.get();
        System.out.println("Rates API called with URL : " + SpecificationQuerier.query(httpRequest).getURI());
        validateStatusCode(getResponseCode(), getResponseMessage(), true);
    }
}
