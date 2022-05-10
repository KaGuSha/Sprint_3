package orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import forall.RestAssuredClient;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersClient extends RestAssuredClient{

    private final String ORDERS = "/api/v1/orders";
    private final String TRACK = "/api/v1/orders/track?t={track}";

    @Step("Send a POST request to create a new order with json to /api/v1/orders")
    public Response sendPostCreateToOrders(Order order) {
        return
                reqSpec
                        .and()
                        .body(order)
                        .when()
                        .post(ORDERS);
    }

    @Step("Send a POST request to create a new order with json to /api/v1/orders")
    public Response sendGetToOrders() {
        return
                reqSpecGet
                        .when()
                        .get(ORDERS);
    }

    @Step("Send a GET request to get a status order with json to /api/v1/orders")
    public Response sendGetToTrackOrder(int track) {
        return reqSpecGet
                        .pathParam("track", track)
                        .when()
                        .get(TRACK);
    }

    @Step("Compare response code and response body about successful creation a new order")
    public void compareResponseCodeAndBodyAboutOrderCreation(Response response) {
        response.then().assertThat()
                .statusCode(201)
                .and()
                .body("track", notNullValue());
    }

    @Step("Compare response code with expected code 200")
    public void compareResponseCode200(Response response) {
        response.then().assertThat().statusCode(200);
    }

    @Step("Check that response body has the list of orders")
    public void isResponseBodyHaveOrdersList(Response response) {
        response.then().assertThat().body("orders.id",notNullValue());
        List<String> orderId = response.then().extract().path("orders.id");

        response.then().body("pageInfo.limit", is(orderId.size()));
        }
}
