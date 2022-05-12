import orders.OrdersClient;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

public class OrderGettingListTest {

    OrdersClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrdersClient();
    }

    @Test
    @DisplayName("Check that the list of orders is contained in the response body")
    @Description("Check that the list of orders is returned in the response body")
    @Severity(SeverityLevel.NORMAL)
    public void checkListOfOrdersContainedInResponseBody() {

        Response response = orderClient.sendGetToOrders();

        orderClient.compareResponseCode200(response);
        orderClient.isResponseBodyHaveOrdersList(response);
    }
}
