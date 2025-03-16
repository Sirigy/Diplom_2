package testsAPI;

import data.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import static data.OrderData.BASE_URL;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@Epic("Получение данных о заказах") // Эпик для Allure-отчётов
public class GetOrderDataTest {

    // Объект для работы с пользователями
    public UserSteps userSteps;
    // Ответ сервера
    public Response response;
    // Токен авторизации
    public String token;
    // Объект для работы с заказами
    public OrderSteps orderSteps;

    @Before
    public void setUp() {
        // Устанавливаем базовый URL для API
        RestAssured.baseURI = BASE_URL;
        // Создаём объекты шагов для работы с пользователями и заказами
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        // Создаём нового пользователя
        response = userSteps.createUser(UserData.getValidUser());
        // Получаем accessToken из ответа
        userSteps.getAccessToken(response);
        token = userSteps.accessToken;
    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    @Description("Этот тест проверяет возможность получения списка заказов для авторизованного пользователя.")
    public void getOrderListForAuthorizedUser() {
        orderSteps.getOrderListAuthorizedUser(token)
                .then()
                .statusCode(SC_OK) // Ожидаемый код 200
                .body("success", equalTo(true)) // Проверяем, что запрос успешен
                .body("orders", notNullValue()) // Проверяем, что список заказов не пуст
                .body("orders.size()", notNullValue()); // Проверяем, что размер списка заказов не null
    }

    @Test
    @DisplayName("Попытка получить заказы конкретного пользователя без авторизации")
    @Description("Этот тест проверяет, что неавторизованный пользователь не может получить заказы " +
            "и получает ошибку 401.")
    public void getFullOrderListForNotAuthorizedUser() {
        orderSteps.getFullOrderListNotAuthorizedUser()
                .then()
                .statusCode(SC_UNAUTHORIZED) // Ожидаемый код 401
                .body("success", equalTo(false)) // Проверяем, что запрос неуспешен
                .body("message", equalTo("You should be authorised")); // Проверяем сообщение ошибки
    }

    @After
    public void tearDown() {
        if (response != null) {
            // Получаем accessToken из ответа
            userSteps.getAccessToken(response);
            // Удаляем тестового пользователя
            userSteps.deleteUser();
        }
    }
}

