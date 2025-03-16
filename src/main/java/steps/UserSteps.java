package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.UserModel;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import static data.OrderData.*;
import static io.restassured.RestAssured.given;

public class UserSteps {

    // Поля для хранения токенов доступа
    public String accessToken;
    public String refreshToken;

    // ========================= Методы =========================

    // Создание нового пользователя
    @Step("Создание уникального пользователя с указанным телом запроса")
    public Response createUser(UserModel userModel) {
        return given()
                .header("Content-type", "application/json") // Установка заголовка JSON
                .body(userModel) // Тело запроса
                .when()
                .post(REGISTER_URL); // Отправка POST-запроса на регистрацию
    }

    // Авторизация пользователя
    @Step("Авторизация пользователя с указанным телом запроса")
    public Response loginUser(UserModel userModel) {
        return given()
                .header("Content-type", "application/json") // Установка заголовка JSON
                .body(userModel) // Тело запроса
                .when()
                .post(LOGIN_URL); // Отправка POST-запроса на вход
    }

    // Удаление пользователя по accessToken
    @Step("Удаление пользователя по accessToken")
    public Response deleteUser() {
        return given()
                .header("Authorization", accessToken) // Передача токена авторизации
                .header("Content-Type", "application/json") // Установка заголовка JSON
                .when()
                .delete(AUTH_URL); // Отправка DELETE-запроса
    }

    // Извлечение токенов доступа из ответа
    @Step("Извлечение accessToken и refreshToken из ответа")
    public void getAccessToken(Response response) {
        this.accessToken = response.jsonPath().getString("accessToken");
        this.refreshToken = response.jsonPath().getString("refreshToken");
    }

    // Обновление данных пользователя с авторизацией
    @Step("Обновление данных пользователя с авторизацией")
    public Response editUserDataWithAuthorization(String accessToken, UserModel userModel) {
        return given()
                .header("Authorization", accessToken) // Передача токена авторизации
                .header("Content-Type", "application/json") // Установка заголовка JSON
                .body(userModel) // Тело запроса
                .when()
                .patch(AUTH_URL); // Отправка PATCH-запроса
    }

    // Обновление данных пользователя без авторизации
    @Step("Обновление данных пользователя без авторизации")
    public Response editUserDataWithoutAuthorization(UserModel userModel) {
        return given()
                .header("Content-Type", "application/json") // Установка заголовка JSON
                .body(userModel) // Тело запроса
                .when()
                .patch(AUTH_URL); // Отправка PATCH-запроса
    }

    // Установка accessToken в локальное хранилище браузера
    @Step("Установка accessToken в localStorage браузера")
    public void setTokenInLocalStorage(WebDriver driver, String token) {
        ((JavascriptExecutor) driver).executeScript(
                "window.localStorage.setItem('accessToken', arguments[0]);", token);
    }

    // Установка refreshToken в локальное хранилище браузера
    @Step("Установка refreshToken в localStorage браузера")
    public void setRefreshTokenInLocalStorage(WebDriver driver, String refreshToken) {
        ((JavascriptExecutor) driver).executeScript(
                "window.localStorage.setItem('refreshToken', arguments[0]);", refreshToken);
    }
}

