import courier.Courier;
import courier.CourierCredentials;
import courier.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CourierCreationTest {

    private int courierId;
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Create a courier with filling in only the required fields")
    @Description("You can create a courier by filling in only the required fields")
    @Severity(SeverityLevel.BLOCKER)
    public void createCourierOnlyRequiredFieldsCreated201() {

        courier = Courier.getRandomRequiredField();

        Response response = courierClient.sendPostCreateToCourier(courier);

        courierClient.compareResponseCodeAndBodyAboutCreation(response);
        CourierCredentials courierCreds = CourierCredentials.from(courier);
        Response responseLogin = courierClient.sendPostToCourierLogin(courierCreds);
        courierClient.compareLoginResponseCodeAndBody200IdNotNull(responseLogin);

    }

    @Test
    @DisplayName("Create a courier with filling in all fields")
    @Description("You can create a courier by filling in all fields")
    @Severity(SeverityLevel.BLOCKER)
    public void createCourierWithAllFieldsCreated201() {

        courier = Courier.getRandom();

        Response response = courierClient.sendPostCreateToCourier(courier);

        courierClient.compareResponseCodeAndBodyAboutCreation(response);
        CourierCredentials courierCreds = CourierCredentials.from(courier);
        Response responseLogin = courierClient.sendPostToCourierLogin(courierCreds);
        courierClient.compareLoginResponseCodeAndBody200IdNotNull(responseLogin);

    }

    @Test
    @DisplayName("Create another courier as an existing courier")
    @Description("You cannot create two identical couriers")
    @Severity(SeverityLevel.NORMAL)
    public void createAnotherCourierAsExistingCourierNotCreated409() {

        courier = Courier.getRandom();
        Response response = courierClient.sendPostCreateToCourier(courier);
        courierClient.compareResponseCodeAndBodyAboutCreation(response);
        Response responseDuplicate = courierClient.sendPostCreateToCourier(courier);
        courierClient.compareResponseCodeAndMessageWithError409(responseDuplicate);
    }

    @Test
    @DisplayName("Create another courier with login which is exist")
    @Description("You cannot create courier with the existing login")
    @Severity(SeverityLevel.NORMAL)
    public void createAnotherCourierWithLoginIsExistNotCreated409() {

        String password = "12345";
        String firstName = "Gulnara";
        courier = Courier.getRandom();
        Courier courierDuplicate = new Courier(courier.getLogin(), password, firstName);

        Response response = courierClient.sendPostCreateToCourier(courier);
        courierClient.compareResponseCodeAndBodyAboutCreation(response);
        Response responseDuplicateLogin = courierClient.sendPostCreateToCourier(courierDuplicate);
        courierClient.compareResponseCodeAndMessageWithError409(responseDuplicateLogin);
    }

    @Test
    @DisplayName("Create a courier with all empty fields")
    @Description("You cannot create courier if the required fields are empty")
    @Severity(SeverityLevel.NORMAL)
    public void createCourierEmptyFieldsNotCreated400() {
        String login = "";
        String password = "";
        String firstName = "";
        courier = new Courier(login, password, firstName);

        Response response = courierClient.sendPostCreateToCourier(courier);

        courierClient.compareCodeAndMessageWithError400(response);
    }

    @Test
    @DisplayName("Create a courier with empty login")
    @Description("You cannot create courier if the required fields are empty")
    @Severity(SeverityLevel.NORMAL)
    public void createCourierEmptyRequiredLoginNotCreated400() {
        String login = "";
        String password = "12345";
        String firstName = "Gulnara";

        courier = new Courier(login, password, firstName);

        Response response = courierClient.sendPostCreateToCourier(courier);

        courierClient.compareCodeAndMessageWithError400(response);
    }

    @Test
    @DisplayName("Create a courier with empty password")
    @Description("You cannot create courier if the required fields are empty")
    @Severity(SeverityLevel.NORMAL)
    public void createCourierEmptyRequiredPasswordNotCreated400() {
        String login = "KaguSha";
        String password = "";
        String firstName = "Gulnara";

        courier = new Courier(login, password, firstName);

        Response response = courierClient.sendPostCreateToCourier(courier);

        courierClient.compareCodeAndMessageWithError400(response);
    }

    @After
    @Step("Getting id and deleting courier")
    public void tearDown() {
        CourierCredentials courierCreds = CourierCredentials.from(courier);
        Response response = courierClient.sendPostToCourierLogin(courierCreds);
        if (response.getStatusCode() == 200) {
            courierId = response.then().extract().path("id");

            if (courierId != 0) {
                courierClient.compareDeleteResponseCodeAndBodyOk(courierClient.sendDelete(courierId));
            }
        }
    }
}
