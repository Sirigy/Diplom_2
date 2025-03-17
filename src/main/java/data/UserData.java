package data;

import com.github.javafaker.Faker;
import models.UserModel;
import java.util.Arrays;
import java.util.List;

import static data.OrderData.BASE_URL;

public class UserData {

    // ========================= Константы =========================
    // Генератор случайных данных Faker
    private static final Faker faker = new Faker();
    // URL для работы с заказами
    public static final String ORDER_URL = BASE_URL + "/api/orders";

    // ========================= Методы =========================

    // Создать валидного пользователя с уникальными данными
    public static UserModel getValidUser() {
        return new UserModel(
                faker.internet().emailAddress(),
                faker.internet().password(7, 20),
                faker.name().firstName()
        );
    }

    // Получить список невалидных пользователей (с отсутствующими полями) для параметризованных тестов
    public static List<UserModel> getInvalidUserRequests() {
        return Arrays.asList(
                new UserModel("", faker.internet().password(7, 20), faker.name().firstName()), // Пустой email
                new UserModel(faker.internet().emailAddress(), "", faker.name().firstName()), // Пустой пароль
                new UserModel(faker.internet().emailAddress(), faker.internet().password(7, 20), "") // Пустое имя
        );
    }

    // Получить список пользователей для тестирования входа с неверными данными
    public static List<UserModel> getInvalidLoginRequests() {
        return Arrays.asList(
                new UserModel("invalidemail@test.com", faker.internet().password(7, 20), null), // Неверный email
                new UserModel(faker.internet().emailAddress(), "incorrectpassword", null), // Неверный пароль
                new UserModel("invalidemail@test.com", "incorrectpassword", null) // Полностью неверные данные
        );
    }

    // Получить список вариантов обновления данных пользователя (изменение email, пароля или имени)
    public static List<UserModel> getUserDataUpdateBodies() {
        return Arrays.asList(
                new UserModel(faker.internet().emailAddress(), null, null), // Обновление email
                new UserModel(null, faker.internet().password(7, 20), null), // Обновление пароля
                new UserModel(null, null, faker.name().lastName()) // Обновление имени
        );
    }
}

