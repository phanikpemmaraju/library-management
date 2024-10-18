package com.library.management.bdd.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.ScenarioScope;
import io.restassured.RestAssured;
import io.restassured.config.ParamConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

import java.util.List;
import java.util.Map;

import static com.library.management.util.FileReader.fromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ScenarioScope
public class ClientSteps {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private RequestSpecification request = createRequest();
    private Response response;

    @Given("the web-service under test is available at {string}")
    public void aBaseUriOf(String baseURI) {
        request.baseUri(baseURI);
    }

    @And("a request body of {string}")
    public void requestBodyFromFile(String fileName) {
        request.body(fromFile(fileName));
    }

    @And("the content type is {string}")
    public void contentTypeIs(String contentType) {
        request.contentType(contentType);
    }

    @When("that POST request is submitted on {string}")
    public void submitPostRequestTo(String path) {
        response = request.post(path);
    }

    @When("that PUT request is submitted on {string}")
    public void submitPutRequestTo(String path) {
        response = request.put(path);
    }

    @When("that DELETE request is submitted on {string}")
    public void submitDeleteRequestTo(String path) {
        response = request.delete(path);
    }

    @When("a GET request is submitted on {string}")
    public void submitGetRequestTo(String path) {
        response = request.get(path);
    }

    @Then("the returned response status is {int}")
    public void statusCodeIs(int statusCode) {
        assertEquals(statusCode, response.getStatusCode());
    }

    @And("the response body matches the content of {string}")
    public void responseBodyMatches(String filename) throws JsonProcessingException, JSONException {
        responseBodyLenientMatch(OBJECT_MAPPER.writeValueAsString(OBJECT_MAPPER.readValue(fromFile(filename), Map.class)));
    }

    @And("the response body matches multiple records the content of {string}")
    public void responseBodyMultipleRecordMatches(String filename) throws JsonProcessingException, JSONException {
        responseBodyLenientMatch(OBJECT_MAPPER.writeValueAsString(OBJECT_MAPPER.readValue(fromFile(filename), new TypeReference<List<Map>>(){})));
    }

    private RequestSpecification createRequest() {
        return RestAssured.given()
                .config(RestAssuredConfig.config().paramConfig(ParamConfig.paramConfig().replaceAllParameters()));
    }

    private void responseBodyLenientMatch(String expectedResponseBody) throws JSONException {
        String actualResponseBody = response.getBody().asString();

        JSONAssert.assertEquals(expectedResponseBody,
                actualResponseBody,
                new CustomComparator(JSONCompareMode.LENIENT));
    }
}
