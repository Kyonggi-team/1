package security.loginsecurity.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import security.loginsecurity.member.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String message;

    private LocalDateTime timestamp;

    private LocalDate chatDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public ChatMessage(String sender, String message, LocalDateTime timestamp, LocalDate chatDate, Member member) {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
        this.chatDate = chatDate;
        this.member = member;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getChatDate() {
        return chatDate;
    }

    public void setChatDate(LocalDate chatDate) {
        this.chatDate = chatDate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

