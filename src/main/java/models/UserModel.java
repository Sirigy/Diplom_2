package models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // Исключить нулевые поля из JSON при сериализации
public class UserModel {

    private String email;
    private String password;
    private String name;

    // Конструктор без аргументов (необходим для Jackson)
    public UserModel() {
    }

    // Конструктор с параметрами
    public UserModel(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // ========================= Геттеры и сеттеры ====
    // Получить email пользователя
    public String getEmail() {
        return email;
    }

    // Установить email пользователя
    public void setEmail(String email) {
        this.email = email;
    }

    // Получить пароль пользователя
    public String getPassword() {
        return password;
    }

    // Установить пароль пользователя
    public void setPassword(String password) {
        this.password = password;
    }

    // Получить имя пользователя
    public String getName() {
        return name;
    }

    // Установить имя пользователя
    public void setName(String name) {
        this.name = name;
    }

    // ======================= Переопределение toString ======
    @Override
    public String toString() {
        return "UserModel{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

