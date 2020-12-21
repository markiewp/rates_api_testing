package stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import rest.ApiConnector;
import rest.ApiValidator;


public class RatesApiStepDefs {
    public static final String API_DEFAULT_URI = "https://api.ratesapi.io/api/";

    ApiConnector apiConnector = new ApiConnector();
    ApiValidator apiValidator = new ApiValidator();

    @Given("Rates API for Latest Foreign Exchange rates")
    public void initializeApiForLatest() {
        apiConnector.buildURI(API_DEFAULT_URI, "latest");
    }

    @Given("Rates API for Specific date Foreign Exchange rates with {string} date")
    public void initializeApiForDate(String date) {
        apiConnector.buildURI(API_DEFAULT_URI, date);
    }

    @Given("Rates API for Latest Foreign Exchange rates is called with {string} URL")
    public void ratesAPIForLatestForeignExchangeRatesWith(String uri) {
        apiConnector.buildURI(uri);
    }

    @When("The API is available")
    public void isApiAvailable() {
        System.out.println("Validating response type...");
        apiValidator.validateResponseType();
    }

    @Then("An automated test suite should run which will assert the success status of the response")
    public void isResponseStatusSuccess() {
        System.out.println("Validating HTTP response message...");
        Assert.assertEquals("ERROR: API is not available or wrong URL provided", "HTTP/1.1 200 OK",
                apiConnector.callApiWithGivenURI().getStatusLine());
    }

    @Then("An automated test suite should run which will assert the response for {string} base and {string} symbol")
    public void validateResponseForBaseAndSymbol(String base, String symbol) {
        apiValidator.validateResponse("latest", base, symbol);
    }

    @Then("An automated test suite should run which will assert the response for {string} date, {string} base and {string} symbol")
    public void validateResponseForBaseAndSymbol(String date, String base, String symbol) {
        apiValidator.validateResponse(date, base, symbol);
    }

    @Then("Test case should assert the correct response supplied by the call for {string}")
    public void testCaseShouldAssertTheCorrectResponseSuppliedByTheCallFor(String url) {
        apiValidator.validateCodesForUrl();
    }
}

