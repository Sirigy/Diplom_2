package tests.api;

import data.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static data.OrderData.BASE_URL;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

@Epic("Изменение данных пользователя") // Эпик для Allure-отчётов
public class EditUserDataWithAuthorizationTest {

    // Объект для работы с пользователями
    private UserSteps userSteps;
    // Ответ сервера
    private Response response;
    // Токен авторизации
    private String token;
    // Объект пользователя с фиксированными данными
    private UserModel fixedUser;

    @Before
    public void setUp() {
        // Устанавливаем базовый URL для API
        RestAssured.baseURI = BASE_URL;
        // Создаём объект шагов для работы с пользователями
        userSteps = new UserSteps();
        // Создаём нового пользователя с уникальными данными
        fixedUser = UserData.getValidUser();
        response = userSteps.createUser(fixedUser);
        // Получаем accessToken из ответа
        userSteps.getAccessToken(response);
        token = userSteps.accessToken;
    }

    @Test
    @DisplayName("Обновление email с авторизацией")
    @Description("Тест проверяет, что email пользователя можно изменить при наличии авторизации.")
    public void editUserDataWithAuthorizationEmailChange() {
        // Создаём объект с новыми данными (только email)
        UserModel emailUpdate = new UserModel("change@chanched.com", null, null);

        // Отправляем запрос на изменение email
        userSteps.editUserDataWithAuthorization(token, emailUpdate)
                .then()
                .statusCode(SC_OK) // Ожидаемый код 200
                .body("success", equalTo(true)) // Ожидаем успешный ответ
                .body("user.email", equalTo("change@chanched.com")) // Проверяем, что email изменился
                .body("user.name", equalTo(fixedUser.getName())); // Имя должно остаться прежним
    }

    @Test
    @DisplayName("Обновление имени с авторизацией")
    @Description("Тест проверяет, что имя пользователя можно изменить при наличии авторизации.")
    public void editUserDataWithAuthorizationNameChange() {
        // Создаём объект с новыми данными (только имя)
        UserModel nameUpdate = new UserModel(null, null, "ChangeMan");

        // Отправляем запрос на изменение имени
        userSteps.editUserDataWithAuthorization(token, nameUpdate)
                .then()
                .statusCode(SC_OK) // Ожидаемый код 200
                .body("success", equalTo(true)) // Ожидаем успешный ответ
                .body("user.email", equalTo(fixedUser.getEmail())) // Email должен остаться прежним
                .body("user.name", equalTo("ChangeMan")); // Проверяем, что имя изменилось
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

