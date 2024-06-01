package security.loginsecurity.memo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import security.loginsecurity.member.Member;
import security.loginsecurity.memo.domain.entity.Memo;
import security.loginsecurity.memo.domain.repository.MemoRepository;
import security.loginsecurity.memo.dto.MemoDto;
import security.loginsecurity.service.MemberService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemoService {
    private final MemoRepository memoRepository;
    private final MemberService memberService;

    @Autowired
    public MemoService(MemoRepository memoRepository, MemberService memberService) {
        this.memoRepository = memoRepository;
        this.memberService = memberService;
    }

    private Member getCurrentMember() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return memberService.findByEmail(userDetails.getUsername());
    }

    public List<MemoDto> getMemosByDate(LocalDate date, String username) {
        Member member = memberService.findByEmail(username);
        List<Memo> memos = memoRepository.findAllByDate(date, member.getId());
        return memos.stream()
                .map(m -> new MemoDto(m.getId(), m.getDate(), m.getContent(), m.getMember().getId()))
                .collect(Collectors.toList());
    }

    public MemoDto getMemoById(Long id) {
        Optional<Memo> memo = memoRepository.findById(id);
        return memo.map(m -> new MemoDto(m.getId(), m.getDate(), m.getContent(), m.getMember().getId())).orElse(null);
    }

    public void appendMemoContent(LocalDate date, String newContent, String username) {
        Member member = memberService.findByEmail(username);
        List<Memo> memos = memoRepository.findAllByDate(date, member.getId());
        if (memos.isEmpty()) {
            Memo newMemo = new Memo(date, newContent, member);
            memoRepository.save(newMemo);
        } else {
            Memo existingMemo = memos.get(0);
            existingMemo.setContent(existingMemo.getContent() + "\n" + newContent);
            memoRepository.save(existingMemo);
        }
    }

    public LocalDate updateMemo(Long id, String content) {
        Optional<Memo> optionalMemo = memoRepository.findById(id);
        if (optionalMemo.isPresent()) {
            Memo memo = optionalMemo.get();
            memo.setContent(content);
            memoRepository.save(memo);
            return memo.getDate();
        }
        return null;
    }
}
