package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // Исключить нулевые поля из JSON при сериализации
public class OrderModel {

    private List<String> ingredients;

    // Конструктор без аргументов (необходим для Jackson)
    public OrderModel() {
    }

    // Конструктор с параметрами
    public OrderModel(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    // ========================= Геттеры и сеттеры =====

    // Получить список ингредиентов
    public List<String> getIngredients() {
        return ingredients;
    }

    // Установить список ингредиентов
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    // ========================= Переопределение toString =========
    @Override
    public String toString() {
        return "OrderModel{" +
                "ingredients=" + ingredients +
                '}';
    }
}

