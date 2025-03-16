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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.UserSteps;

import static data.OrderData.BASE_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

@Epic("Авторизация пользователя") // Эпик для Allure-отчётов
@RunWith(Parameterized.class)
public class InvalidUserLoginParameterizedTest {

    // Индекс набора данных (указывает, какие некорректные данные используются: email, пароль или оба)
    private final int index;
    // Объект для работы с пользователями
    private UserSteps userSteps;
    // Ответ от сервера при создании пользователя
    private Response uniqueUserCreating;
    // Ответ от сервера при попытке входа
    private Response response;

    // Конструктор, принимающий индекс набора данных.
    public InvalidUserLoginParameterizedTest(int index) {
        this.index = index;
    }

    /*Метод, предоставляющий параметры для параметризованных тестов.
     Индексы соответствуют разным комбинациям некорректных данных:
     0: неправильный email
     1: неправильный пароль
     2: неправильный email и пароль
     */
    @Parameterized.Parameters(name = "Тест - вход с некорректными данными, индекс: {0}")
    public static Object[] invalidUserIndices() {
        return new Object[]{0, 1, 2};
    }

    @Before
    public void setUp() {
        // Устанавливаем базовый URL для API
        RestAssured.baseURI = BASE_URL;
        // Создаём объект шагов для работы с пользователями
        userSteps = new UserSteps();
        // Создаём уникального пользователя для проверки входа
        uniqueUserCreating = userSteps.createUser(UserData.getValidUser());
    }

    @Test
    @DisplayName("Попытка входа с некорректными данными")
    @Description("Этот тест проверяет, что при попытке авторизоваться с некорректным email, паролем или обоими " +
            "возвращается ошибка с кодом 401 и соответствующим сообщением.")
    public void userLoginWithInvalidData() {
        // Отправляем запрос на авторизацию с некорректными данными
        response = userSteps.loginUser(UserData.getInvalidLoginRequests().get(index));

        // Проверяем, что сервер вернул ошибку 401
        response.then()
                .statusCode(SC_UNAUTHORIZED) // Ожидаемый код 401
                .body("success", equalTo(false)) // Ожидаем, что запрос неуспешен
                .body("message", equalTo("email or password are incorrect")); // Проверяем сообщение ошибки
    }

    @After
    public void tearDown() {
        if (uniqueUserCreating != null) {
            // Получаем accessToken из ответа
            userSteps.getAccessToken(uniqueUserCreating);
            // Удаляем тестового пользователя
            userSteps.deleteUser();
        }
    }
}

