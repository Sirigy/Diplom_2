package testsAPI;

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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

@Epic("Создание пользователей") // Эпик для Allure-отчётов
public class CreateUserTest {

    // Объект для работы с пользователями
    public UserSteps userSteps;
    // Ответ сервера
    public Response response;

    @Before
    public void setUp() {
        // Устанавливаем базовый URL для API
        RestAssured.baseURI = BASE_URL;
        // Создаём объект шагов для работы с пользователями
        userSteps = new UserSteps();
    }

    // ========================= Тесты =========================

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Этот тест проверяет возможность создания пользователя с валидными данными.")
    public void uniqueUserCreating() {
        // Создаём нового уникального пользователя
        response = userSteps.createUser(UserData.getValidUser());

        // Проверяем, что запрос успешен
        response.then()
                .statusCode(SC_OK) // Ожидаемый код 200
                .body("success", equalTo(true)); // Проверяем, что пользователь успешно создан
    }

    @Test
    @DisplayName("Создание дубликата пользователя")
    @Description("Этот тест проверяет, что повторная регистрация одного и того же пользователя " +
            "возвращает ошибку с кодом 403 и соответствующим сообщением.")
    public void duplicateUserCreationReturnsError() {
        // Создаём пользователя с фиксированными данными
        UserModel fixedUser = UserData.getValidUser();

        // Отправляем первый запрос на создание пользователя
        response = userSteps.createUser(fixedUser);

        // Отправляем повторный запрос с теми же данными
        Response responseSecondCreation = userSteps.createUser(fixedUser);

        // Проверяем, что сервер вернул ошибку 403
        responseSecondCreation.then()
                .statusCode(SC_FORBIDDEN) // Ожидаемый код 403
                .body("success", equalTo(false)) // Ожидаем, что запрос неуспешен
                .body("message", equalTo("User already exists")); // Проверяем сообщение ошибки
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

