package security.loginsecurity.memo.dto;

import java.time.LocalTime;

public class SleepDto {
    private Long id;
    private LocalTime start;
    private LocalTime end;

    public SleepDto() {
    }

    public SleepDto(Long id, LocalTime start, LocalTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public int getDurationInMinutes() {
        return end.toSecondOfDay() / 60 - start.toSecondOfDay() / 60;
    }
}
