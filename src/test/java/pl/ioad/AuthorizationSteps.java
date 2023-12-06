package pl.ioad;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import pl.ioad.entity.Login;
import pl.ioad.entity.UserCreate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthorizationSteps {

    private static final String BASE_URL = "https://api.practicesoftwaretesting.com";
    private static Response response;
    private static String randomEmail;

    @Given("a login API endpoint")
    public void a_login_api_endpoint() {
        RestAssured.baseURI = BASE_URL + "/users/login";
    }

    @When("the user submits a POST request with login data")
    public void the_user_submits_a_post_request_with_the_following_data(DataTable table) {
        Map<String, String> requestBody = table.asMap(String.class, String.class);

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post();
    }

    @Then("a {int} status is received")
    public void a_status_is_received(int arg0) {
        assertEquals(arg0, response.getStatusCode());
    }

    @And("the response contains access token")
    public void the_response_contains_the_logged_in_user_s_email() {
        Map<String, String> responseBody = response.jsonPath().get();
        assertTrue(responseBody.containsKey("access_token"));
    }

    @Given("a register API endpoint")
    public void aRegisterAPIEndpoint() {
        RestAssured.baseURI = BASE_URL + "/users/register";
    }

    @When("the user submits a POST request with random registration data")
    public void theUserSubmitsAPOSTRequestWithRandomRegistrationData() {
        randomEmail = "user" + System.currentTimeMillis() + "@doe.example";

        var userData = new UserCreate(
                "John",
                "Doe",
                "Street 1",
                "City",
                "State",
                "Country",
                "1234AA",
                "0987654321",
                "1970-01-01",
                randomEmail,
                "super-secret"
        );

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post();
    }

    @When("the user submits a POST request with random login data")
    public void theUserSubmitsAPOSTRequestWithRandomLoginData() {
        var loginData = new Login(
                randomEmail,
                "super-secret"
        );

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post();
    }

    @When("the user submits a POST request with registration data containing existing email")
    public void theUserSubmitsAPOSTRequestWithRegistrationDataContainingExistingEmail() {
        var userData = new UserCreate(
                "John",
                "Doe",
                "Street 1",
                "City",
                "State",
                "Country",
                "1234AA",
                "0987654321",
                "1970-01-01",
                "customer@practicesoftwaretesting.com",
                "super-secret"
        );

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post();
    }

    @And("the response contains error {string} message with text {string}")
    public void theResponseContainsErrorEmailMessageWithTextACustomerWithThisEmailAddressAlreadyExists(String key, String message) {
        var responseBody = response.jsonPath().getList(key);
        assertEquals(message, responseBody.get(0));
    }
}
