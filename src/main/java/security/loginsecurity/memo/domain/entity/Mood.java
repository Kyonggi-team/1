package security.loginsecurity.memo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import security.loginsecurity.member.Member;

import java.time.LocalDate;


@Getter
@Setter
@Entity
public class Mood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String mood;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Mood() {}

    public Mood(LocalDate date, String mood) {
        this.date = date;
        this.mood = mood;
    }


}