package security.loginsecurity.memo.dto;

import java.time.LocalDate;

public class MoodDto {
    private Long id;
    private LocalDate date;
    private String mood;

    public MoodDto() {}

    public MoodDto(Long id, LocalDate date, String mood) {
        this.id = id;
        this.date = date;
        this.mood = mood;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}
