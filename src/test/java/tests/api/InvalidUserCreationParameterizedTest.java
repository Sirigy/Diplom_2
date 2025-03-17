package tests.api;

import data.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.UserSteps;

import static data.OrderData.BASE_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

@Epic("Создание пользователей") // Эпик для Allure-отчётов
@RunWith(Parameterized.class)
public class InvalidUserCreationParameterizedTest {

    // Индекс набора данных (указывает, какое поле отсутствует: email, пароль или имя)
    private final int index;
    // Объект для работы с пользователями
    private UserSteps userSteps;
    // Ответ сервера
    private Response response;

   // Конструктор, принимающий индекс набора данных.
    public InvalidUserCreationParameterizedTest(int index) {
        this.index = index;
    }

    /*Метод, предоставляющий параметры для параметризованных тестов.
     Каждый индекс соответствует пользователю без одного из обязательных полей:
     0: без email
     1: без пароля
     2: без имени
     */
    @Parameterized.Parameters(name = "Тест - создание пользователя без одного из обязательных полей, индекс: {0}")
    public static Object[] invalidUserIndices() {
        return new Object[]{0, 1, 2};
    }

    @Before
    public void setUp() {
        // Устанавливаем базовый URL для API
        RestAssured.baseURI = BASE_URL;
        // Создаём объект шагов для работы с пользователями
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Создание пользователя с отсутствующим обязательным полем")
    @Description("Этот тест проверяет, что при попытке создать пользователя без email, пароля или имени " +
            "возвращается ошибка с кодом 403 и соответствующим сообщением.")
    public void shouldReturnErrorForInvalidUser() {
        // Отправляем запрос на создание пользователя с отсутствующим обязательным полем
        response = userSteps.createUser(UserData.getInvalidUserRequests().get(index));

        // Проверяем, что сервер вернул ошибку 403
        response.then()
                .statusCode(SC_FORBIDDEN) // Ожидаемый код 403
                .body("success", equalTo(false)) // Ожидаем, что запрос неуспешен
                .body("message", equalTo("Email, password and name are required fields")); // Проверяем сообщение ошибки
    }
}

