package security.loginsecurity.chat.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.loginsecurity.chat.entity.ChatMessage;
import security.loginsecurity.member.Member;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatDateAndMember(LocalDate chatDate, Member member);
    List<ChatMessage> findBySenderAndChatDateAndMemberOrderByTimestampAsc(String sender, LocalDate chatDate, Member member);
}

