package security.loginsecurity.chat.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.loginsecurity.chat.Repository.SummaryRepository;
import security.loginsecurity.chat.entity.Summary;
import security.loginsecurity.member.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SummaryService {

    @Autowired
    private SummaryRepository summaryRepository;

    public Summary saveSummary(String content, LocalDate chatDate, Member member) {
        Summary summary = new Summary(content, chatDate, member);
        return summaryRepository.save(summary);
    }

    public List<Summary> getAllSummaries() {
        return summaryRepository.findAll();
    }

    public List<Summary> getAllSummariesByMember(Member member) {
        return summaryRepository.findByMember(member);
    }

    public Optional<Summary> getSummaryByDateAndMember(LocalDate chatDate, Member member) {
        return summaryRepository.findByChatDateAndMember(chatDate, member);
    }
}
