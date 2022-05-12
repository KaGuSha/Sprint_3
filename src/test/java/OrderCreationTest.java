import orders.*;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    private final List<String> colorScooter;
    int track;
    OrdersClient orderClient;

    public OrderCreationTest(List<String> colorScooter) {
        this.colorScooter = colorScooter;
    }

    @Parameterized.Parameters
    public static Object[] getOrderCreation() {
        return new Object[][]{
                {List.of()},
                {List.of("BLACK", "GREY")},
                {List.of("GREY")},
                {List.of("BLACK")},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        orderClient = new OrdersClient();
    }

    @Test
    @DisplayName("Create an order by scooter color")
    @Description("You can specify one of the colors, both colors or you can not specify the color at all")
    @Severity(SeverityLevel.NORMAL)
    public void orderCreateByScooterColor() {

        Order order = Order.createOrderWithColor(colorScooter);

        Response response = orderClient.sendPostCreateToOrders(order);

        orderClient.compareResponseCodeAndBodyAboutOrderCreation(response);
        track = response.then().extract().path("track");
        Response responseGet = orderClient.sendGetToTrackOrder(track);
        orderClient.compareResponseCode200(responseGet);
    }
}
