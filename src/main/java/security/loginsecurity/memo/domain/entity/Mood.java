package security.loginsecurity.memo.domain.entity;

import jakarta.persistence.*;
import security.loginsecurity.member.Member;

import java.time.LocalDate;

@Entity
public class Mood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String mood;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Mood() {}

    public Mood(LocalDate date, String mood) {
        this.date = date;
        this.mood = mood;
        this.member =member;
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
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}