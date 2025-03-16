package testsAPI;

import data.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.UserModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.UserSteps;

import java.util.List;

import static data.OrderData.BASE_URL;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

@Epic("Изменение данных пользователя") // Эпик для Allure-отчётов
@RunWith(Parameterized.class)
public class EditUserDataWithoutAuthorizationTest {

    // Данные для обновления пользователя
    private final UserModel userData;
    // Объект для работы с пользователями
    private UserSteps userSteps;

    // Конструктор, принимающий параметры для теста.
    public EditUserDataWithoutAuthorizationTest(UserModel userData) {
        this.userData = userData;
    }

    // Метод, предоставляющий тестовые данные для параметризованных тестов.
    @Parameterized.Parameters(name = "Тест с набором данных для обновления без авторизации: {0}")
    public static List<UserModel> userData() {
        return UserData.getUserDataUpdateBodies();
    }

    @Before
    public void setUp() {
        // Устанавливаем базовый URL для API
        RestAssured.baseURI = BASE_URL;
        // Создаём объект шагов для работы с пользователями
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Обновление данных пользователя без авторизации")
    @Description("Этот тест проверяет, что при попытке обновить данные пользователя без авторизации " +
            "возвращается ошибка с кодом 401 и соответствующим сообщением.")
    public void shouldReturnErrorWhenEditingWithoutAuthorization() {
        // Отправляем запрос на обновление данных без авторизации
        Response response = userSteps.editUserDataWithoutAuthorization(userData);

        // Проверяем, что сервер вернул ошибку 401
        response.then()
                .statusCode(SC_UNAUTHORIZED) // Ожидаемый код 401
                .body("success", equalTo(false)) // Ожидаем, что запрос неуспешен
                .body("message", equalTo("You should be authorised")); // Проверяем сообщение ошибки
    }
}

