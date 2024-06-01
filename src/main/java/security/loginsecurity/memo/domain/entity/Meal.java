package security.loginsecurity.memo.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;  // 날짜

    private String mealType; // 식사 종류 (아침, 점심, 저녁, 간식)


    private LocalTime time;  // 식사 시간
    private String menus;    // 메뉴 목록, 쉼표로 구분된 문자열
    private int rating;      // 식사 평가 점수

    // Constructor
    public Meal() {
    }

    public Meal(LocalDate date, String mealType, LocalTime time, String menus, int rating) {
        this.date = date;
        this.mealType = mealType;
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

    public String getMenus() {
        return menus;
    }

    public int getRating() {
        return rating;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}