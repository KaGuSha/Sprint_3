import courier.*;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;


import io.restassured.response.Response;


public class CourierLoginTest {
    private int courierId;
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = Courier.getRandom();
        courierClient.sendPostCreateToCourier(courier);
    }

    @Test
    @DisplayName("Check Login wtih creds of the courier")
    @Description("You can log in with correct creds of courier")
    @Severity(SeverityLevel.BLOCKER)
    public void checkCourierCanLoginResponse200IdNotNull() {

        CourierCredentials courierCredCorrect = CourierCredentials.from(courier);
        Response response = courierClient.sendPostToCourierLogin(courierCredCorrect);
        courierClient.compareLoginResponseCodeAndBody200IdNotNull(response);
    }


    @Test
    @DisplayName("Check log in with empty login and password fields")
    @Description("You cannot login without all required fields")
    @Severity(SeverityLevel.NORMAL)
    public void checkLoginCourierWitEmptyLoginPasswordCode400() {

        CourierCredentials courierCredsIncorrect = new CourierCredentials("", "");
        Response response = courierClient.sendPostToCourierLogin(courierCredsIncorrect);
        courierClient.compareLoginResponseCodeAndBody400Message(response);
    }

    @Test
    @DisplayName("Check log in with empty login field")
    @Description("You cannot login without all required fields")
    @Severity(SeverityLevel.NORMAL)
    public void checkLoginCourierWitEmptyLoginCode400() {

        CourierCredentials courierCredsIncorrect = new CourierCredentials("", courier.getPassword());
        Response response = courierClient.sendPostToCourierLogin(courierCredsIncorrect);
        courierClient.compareLoginResponseCodeAndBody400Message(response);
    }

    @Test
    @DisplayName("Check log in with empty password field")
    @Description("You cannot login without all required fields")
    @Severity(SeverityLevel.NORMAL)
    public void checkLoginCourierWitEmptyPasswordCode400() {

        CourierCredentials courierCredsIncorrect = new CourierCredentials(courier.getLogin(), "");
        Response response = courierClient.sendPostToCourierLogin(courierCredsIncorrect);

        courierClient.compareLoginResponseCodeAndBody400Message(response);
    }

    @Test
    @DisplayName("Check log in with incorrect login field")
    @Description("You cannot log in as a non-existent user, the request returns an error")
    @Severity(SeverityLevel.NORMAL)
    public void checkLoginCourierIncorrectLogin404() {

        CourierCredentials courierCredsIncorrect = new CourierCredentials("KaguSha_454", courier.getPassword());
        Response response = courierClient.sendPostToCourierLogin(courierCredsIncorrect);
        courierClient.compareLoginResponseCodeAndBody404Message(response);
    }

    @Test
    @DisplayName("Check log in with incorrect password field")
    @Description("You cannot log in as a non-existent user, the request returns an error")
    @Severity(SeverityLevel.NORMAL)
    public void checkLoginCourierIncorrectPassword404() {

        CourierCredentials courierCredsIncorrect = new CourierCredentials(courier.getLogin(), "123456");
        Response response = courierClient.sendPostToCourierLogin(courierCredsIncorrect);
        courierClient.compareLoginResponseCodeAndBody404Message(response);
    }

    @After
    public void tearDown() {
        CourierCredentials courierCredsCorrect = CourierCredentials.from(courier);

        courierId = courierClient.sendPostToCourierLogin(courierCredsCorrect)
                .then().extract().path("id");

        Response response = courierClient.sendDelete(courierId);
        courierClient.compareDeleteResponseCodeAndBodyOk(response);
    }
}
