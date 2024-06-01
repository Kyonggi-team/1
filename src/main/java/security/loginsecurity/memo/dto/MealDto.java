package security.loginsecurity.memo.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MealDto {
    private Long id;
    private String mealType;
    private LocalDate date;
    private LocalTime time;
    private List<String> menus;
    private int rating;

    // Constructor
    public MealDto() {
    }

    public MealDto(Long id, String mealType, LocalDate date, LocalTime time, List<String> menus, int rating) {
        this.id = id;

        this.mealType=mealType;

        this.date = date;
        this.time = time;
        this.menus = menus;
        this.rating = rating;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getMealType() {
        return mealType;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public List<String> getMenus() {
        return menus;
    }

    public int getRating() {
        return rating;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setMenus(List<String> menus) {
        this.menus = menus;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}