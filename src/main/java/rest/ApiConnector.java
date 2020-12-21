package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiConnector {
    ObjectMapper objectMapper = new ObjectMapper();

    public void buildURI(String uri) {
        buildURI(uri, "");
    }

    public void buildURI(String uri, String date) {
        RestAssured.baseURI = uri;
        RestAssured.basePath = date;
        System.out.println("Setting API base URL: " + uri + date);
    }

    public Response callApiWithGivenURI() {
        RequestSpecification httpRequest = RestAssured.given();
        return httpRequest.request(Method.GET);
    }

    public RatesAPIResponse getRatesResponseObject(String ratesResponseJSON) {
        try {
            return objectMapper.readValue(ratesResponseJSON, RatesAPIResponse.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}


