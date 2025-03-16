package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.OrderModel;

import static data.UserData.ORDER_URL;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    // ========================= Методы =========================

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuthorization(String accessToken, OrderModel orderModel) {
        return given()
                .header("Authorization", accessToken) // Передача токена авторизации
                .header("Content-Type", "application/json") // Установка заголовка JSON
                .body(orderModel) // Тело запроса
                .when()
                .post(ORDER_URL); // Отправка POST-запроса
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuthorization(OrderModel orderModel) {
        return given()
                .header("Content-Type", "application/json") // Установка заголовка JSON
                .body(orderModel) // Тело запроса
                .when()
                .post(ORDER_URL); // Отправка POST-запроса
    }

    @Step("Получение списка заказов авторизованного пользователя")
    public Response getOrderListAuthorizedUser(String token) {
        return given()
                .header("Authorization", token) // Передача токена авторизации
                .when()
                .get(ORDER_URL); // Отправка GET-запроса
    }

    @Step("Получение списка заказов неавторизованного пользователя")
    public Response getFullOrderListNotAuthorizedUser() {
        return given()
                .when()
                .get(ORDER_URL); // Отправка GET-запроса без токена
    }
}

