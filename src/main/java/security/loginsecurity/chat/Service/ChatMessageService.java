package security.loginsecurity.chat.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.loginsecurity.chat.Repository.ChatMessageRepository;
import security.loginsecurity.chat.entity.ChatMessage;
import security.loginsecurity.member.Member;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessagesByDateAndMember(LocalDate date, Member member) {
        return chatMessageRepository.findByChatDateAndMember(date, member);
    }

    public List<ChatMessage> getUserMessagesByDateAndMember(LocalDate date, Member member) {
        return chatMessageRepository.findBySenderAndChatDateAndMemberOrderByTimestampAsc("user", date, member);
    }
}

