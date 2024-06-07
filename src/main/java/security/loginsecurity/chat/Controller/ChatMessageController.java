package security.loginsecurity.chat.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import security.loginsecurity.chat.Service.ChatMessageService;
import security.loginsecurity.chat.Service.GptService;
import security.loginsecurity.chat.Service.SummaryService;
import security.loginsecurity.chat.entity.ChatMessage;
import security.loginsecurity.chat.entity.Summary;
import security.loginsecurity.member.Member;
import security.loginsecurity.repository.MemberRepository;
import security.loginsecurity.service.MemberService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private GptService gptService;

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private MemberService memberService; // MemberService 주입

    private MemberRepository memberRepository;

    @GetMapping("/chat")
    public String chat(@RequestParam("date") String date, @RequestParam("memberId") Long memberId, Model model) {
        LocalDate chatDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        Member member = memberService.findById(memberId);

        if (member == null) {
            throw new RuntimeException("Member not found for id: " + memberId);
        }

        List<ChatMessage> messages = chatMessageService.getMessagesByDateAndMember(chatDate, member);

        // "대화시작" 메시지가 없다면 추가
        if (messages.isEmpty()) {
            ChatMessage startMessage = new ChatMessage("assistant", "일기 작성을 위한 대화를 원하시면 \"대화시작\"을 입력해주세요.", LocalDateTime.now(), chatDate, member);
            chatMessageService.saveMessage(startMessage);
            messages.add(startMessage);
        }

        model.addAttribute("messages", messages);
        model.addAttribute("chatDate", date);
        model.addAttribute("memberId", memberId);
        return "chat";
    }

    @PostMapping("/chat/message")
    public String saveMessage(@RequestParam("message") String message, @RequestParam("role") String role, @RequestParam("date") String date, @RequestParam("memberId") Long memberId, Model model) {
        LocalDate chatDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        Member member = memberService.findById(memberId);

        if (member == null) {
            throw new RuntimeException("Member not found for id: " + memberId);
        }

        // 사용자 메시지 저장
        ChatMessage userMessage = new ChatMessage("user", message, LocalDateTime.now(), chatDate, member);
        chatMessageService.saveMessage(userMessage);

        // 모든 메시지 가져오기
        List<ChatMessage> allMessages = chatMessageService.getMessagesByDateAndMember(chatDate, member);

        // GPT 응답 가져오기
        String response = gptService.getGptResponse(allMessages, role);

        // GPT 응답 메시지 저장
        ChatMessage gptMessage = new ChatMessage("assistant", response, LocalDateTime.now(), chatDate, member);
        chatMessageService.saveMessage(gptMessage);

        model.addAttribute("messages", chatMessageService.getMessagesByDateAndMember(chatDate, member));
        model.addAttribute("chatDate", date);
        model.addAttribute("memberId", memberId);
        return "chat";
    }




    @GetMapping("/chatSummary")
    public String getSummary(@RequestParam("date") String date, @RequestParam("memberId") Long memberId, Model model) {
        LocalDate chatDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        Member member = memberService.findById(memberId);

        if (member == null) {
            throw new RuntimeException("Member not found for id: " + memberId);
        }

        Optional<Summary> existingSummary = summaryService.getSummaryByDateAndMember(chatDate, member);

        if (existingSummary.isPresent()) {
            model.addAttribute("summary", existingSummary.get().getContent());
        } else {
            List<ChatMessage> userMessages = chatMessageService.getUserMessagesByDateAndMember(chatDate, member);
            String summaryContent = gptService.summarizeMessages(userMessages);
            summaryService.saveSummary(summaryContent, chatDate, member);
            model.addAttribute("summary", summaryContent);
        }
        model.addAttribute("chatDate", date);
        model.addAttribute("memberId", memberId);
        return "chatSummary";
    }

    @GetMapping("/summaries")
    public String getAllSummaries(@RequestParam Long memberId, Model model) {
        Member member = memberService.findById(memberId); // 사용자 ID로 사용자 조회
        if (member == null) {
            throw new RuntimeException("Member not found for id: " + memberId);
        }
        List<Summary> summaries = summaryService.getAllSummariesByMember(member); // 사용자 ID를 기반으로 요약 목록 조회
        model.addAttribute("summaries", summaries);
        return "summaries";
    }
}

