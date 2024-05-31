package security.loginsecurity.memo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SleepDto {
    private Long id;
    private LocalDate date;  // 수정된 부분
    private LocalTime start;
    private LocalTime end;
    private Long memberId;


    public SleepDto() {
    }

    public SleepDto(Long id, LocalDate date, LocalTime start, LocalTime end, Long memberId) {  // 수정된 부분
        this.id = id;
        this.date = date;
        this.start = start;
        this.end = end;
        this.memberId = memberId;

    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {  // 수정된 부분
        return date;
    }

    public void setDate(LocalDate date) {  // 수정된 부분
        this.date = date;
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

    public Long getMemberId() {
        return memberId;
    }


    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

}
