package tests.api;

import data.OrderData;
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
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@Epic("Создание заказов") // Эпик для Allure-отчётов
public class CreateOrderTest {

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
        // Создаём объекты шагов для работы с пользователем и заказами
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        // Создаём нового пользователя
        response = userSteps.createUser(UserData.getValidUser());
        // Получаем accessToken из ответа
        userSteps.getAccessToken(response);
        token = userSteps.accessToken;
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Тест проверяет возможность создания заказа с авторизацией. " +
            "Проверяются ключевые поля ответа: ID, статус, цена и другие.")
    public void createOrderWithAuthorization() {
        orderSteps.createOrderWithAuthorization(token, OrderData.getOrderBodies().get(5))
                .then()
                .statusCode(SC_OK) // Ожидаемый код 200
                .body("success", equalTo(true)) // Успешный статус запроса
                .body("name", notNullValue()) // Имя заказа присутствует
                .body("order", notNullValue()) // Должен быть объект "order"
                .body("order._id", notNullValue()) // ID заказа должен присутствовать
                .body("order.owner", notNullValue()) // Владелец заказа должен присутствовать
                .body("order.status", equalTo("done")) // Проверяем статус заказа
                .body("order.name", notNullValue()) // Имя заказа присутствует
                .body("order.createdAt", notNullValue()) // Дата создания заказа
                .body("order.updatedAt", notNullValue()) // Дата обновления заказа
                .body("order.number", notNullValue()) // Номер заказа присутствует
                .body("order.price", notNullValue()); // Цена заказа присутствует
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Тест проверяет возможность создания заказа без авторизации. " +
            "Проверяется, что заказ успешно создаётся, но без привязки к пользователю.")
    public void createOrderWithoutAuthorization() {
        orderSteps.createOrderWithoutAuthorization(OrderData.getOrderBodies().get(4))
                .then()
                .statusCode(SC_OK) // Ожидаемый код 200
                .body("success", equalTo(true)) // Успешный статус запроса
                .body("name", notNullValue()) // Имя заказа присутствует
                .body("order", notNullValue()); // Должен быть объект "order"
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Тест проверяет, что при попытке создать заказ без ингредиентов " +
            "возвращается ошибка с кодом 400 и соответствующим сообщением.")
    public void createOrderWithoutIngredients() {
        orderSteps.createOrderWithAuthorization(token, OrderData.getEmptyIngredients())
                .then()
                .statusCode(SC_BAD_REQUEST) // Ожидаемый код 400
                .body("success", equalTo(false)) // Ожидаем неуспешный статус запроса
                .body("message", equalTo("Ingredient ids must be provided")); // Проверяем сообщение ошибки
    }

    @Test
    @DisplayName("Создание заказа с недопустимым хешем ингредиента")
    @Description("Тест проверяет, что при передаче неверного хеша ингредиента " +
            "сервер возвращает ошибку 500 (ошибка сервера). " +
            "Это крайний случай, так как обычно ошибки 500 нежелательны.")
    public void createOrderWithWrongHashForIngredients() {
        orderSteps.createOrderWithAuthorization(token, OrderData.getInvalidHashIngredient())
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR); // Ожидаемый код 500
    }

    @After
    public void tearDown() {
        if (response != null) {
            // Повторно извлекаем accessToken (на случай, если он изменился)
            userSteps.getAccessToken(response);
            // Удаляем тестового пользователя
            userSteps.deleteUser();
        }
    }
}

