package security.loginsecurity.memo.dto;
import java.time.LocalDate;
public class MemoDto {
    private Long id;
    private LocalDate date;
    private String content;
    private Long memberId;

    // 기본 생성자 추가
    public MemoDto() {}

    // 모든 필드를 초기화하는 생성자
    public MemoDto(Long id, LocalDate date, String content, Long memberId) {
        this.id = id;
        this.date = date;
        this.content = content;
        this.memberId = memberId;

    }

    public MemoDto(LocalDate date, String content) {
        this.date = date;
        this.content = content;
    }

    // 게터와 세터 메서드
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

}
