package courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import forall.RestAssuredClient;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class CourierClient extends RestAssuredClient {

    private final String ROOT = "/api/v1/courier";
    private final String LOGIN = ROOT + "/login";
    private final String DELETE = ROOT + "/{courierId}";

    @Step("Send a POST request to create an account for the courier with json to /api/v1/courier")
    public Response sendPostCreateToCourier(Courier courier) {
        return reqSpec
                .and()
                .body(courier)
                .when()
                .post(ROOT);
    }

    @Step("Send a POST request to login with json to /api/v1/courier/login")
    public Response sendPostToCourierLogin(CourierCredentials courierLogin) {
        return reqSpec
                .and()
                .body(courierLogin)
                .when()
                .post(LOGIN);
    }

    @Step("Send a DELETE request to remove courier to /api/v1/courier/{id}")
    public Response sendDelete(int courierId) {

        String json = "{\"id\": \"" + courierId + "\"}";

        return reqSpec
                .pathParam("courierId", courierId)
                .and()
                .body(json)
                .when()
                .delete(DELETE);
    }

    @Step("Compare response code and body with expected code 201 and ok has true")
    public void compareResponseCodeAndBodyAboutCreation(Response response) {
        response.then().assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", is(true));
    }

    @Step("Compare response code and body with expected code 200 and ok has true")
    public void compareDeleteResponseCodeAndBodyOk(Response response) {
        response.then().assertThat()
                .statusCode(SC_OK)
                .and()
                .body("ok", is(true));
    }

    @Step("Compare response code and message with expected code 409 and expected text of message - Этот логин уже используется")
    public void compareResponseCodeAndMessageWithError409(Response response) {
        response.then().assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", is("Этот логин уже используется"));
        //отличается текст startsWith
    }

    @Step("Compare response code and message with expected code 400 and expected text of message - Недостаточно данных для создания учетной записи")
    public void compareCodeAndMessageWithError400(Response response) {
        response.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Step("Compare response code with expected code 200 and id has not null value")
    public void compareLoginResponseCodeAndBody200IdNotNull(Response response) {
        response.then().assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());
    }

    @Step("Compare response code and message with expected code 400 and expected text of message - Недостаточно данных для входа")
    public void compareLoginResponseCodeAndBody400Message(Response response) {
        response.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для входа"));
    }

    @Step("Compare response code and message with expected code 404 and expected text of message - Учетная запись не найдена")
    public void compareLoginResponseCodeAndBody404Message(Response response) {
        response.then().assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", is("Учетная запись не найдена"));
    }
}
