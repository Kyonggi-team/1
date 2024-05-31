package security.loginsecurity.memo.domain.entity;

import jakarta.persistence.*;
import security.loginsecurity.member.Member;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Sleep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date; // 날짜

    private LocalTime start;
    private LocalTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;



    public Sleep() {
    }


    public Sleep(LocalDate date, LocalTime start, LocalTime end, Member member) {
        this.date = date;
        this.start = start;
        this.end = end;
        this.member=member;
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




    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public Member getMember() {
        return member;
    }


    public void setMember(Member member) {
        this.member = member;
    }


}

