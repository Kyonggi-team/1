package security.loginsecurity.exercise;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import security.loginsecurity.member.Member;


@Entity
@Table(name = "survey_result")
@Getter
public class SurveyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "total_score")
    private int totalScore;

    @Column(name = "survey_month")
    private String surveyMonth;

    @Builder
    public SurveyResult(Member member, int totalScore, String surveyMonth) {
        this.member=member;
        this.totalScore=totalScore;
        this.surveyMonth=surveyMonth;
    }
}