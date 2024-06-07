package security.loginsecurity.chat.entity;

import jakarta.persistence.*;
import security.loginsecurity.member.Member;

import java.time.LocalDate;

@Entity
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private LocalDate chatDate;

    public Summary() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Summary(String content, LocalDate chatDate, Member member) {
        this.content = content;
        this.chatDate = chatDate;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getChatDate() {
        return chatDate;
    }

    public void setChatDate(LocalDate chatDate) {
        this.chatDate = chatDate;
    }
}
