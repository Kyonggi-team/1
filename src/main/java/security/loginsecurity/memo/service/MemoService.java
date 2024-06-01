package security.loginsecurity.memo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.loginsecurity.memo.domain.entity.Memo;
import security.loginsecurity.memo.domain.repository.MemoRepository;
import security.loginsecurity.memo.dto.MemoDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemoService {
    private final MemoRepository memoRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    // 특정 날짜에 대한 메모 리스트를 가져오는 메서드
    public List<MemoDto> getMemosByDate(LocalDate date) {
        List<Memo> memos = memoRepository.findAllByDate(date);
        return memos.stream()
                .map(m -> new MemoDto(m.getId(), m.getDate(), m.getContent()))
                .collect(Collectors.toList());
    }

    // 특정 ID로 메모를 조회하는 메서드
    public MemoDto getMemoById(Long id) {
        Optional<Memo> memo = memoRepository.findById(id);
        return memo.map(m -> new MemoDto(m.getId(), m.getDate(), m.getContent())).orElse(null);
    }

    // 기존 메모에 새로운 내용을 추가하는 메서드
    public void appendMemoContent(LocalDate date, String newContent) {
        List<Memo> memos = memoRepository.findAllByDate(date);
        if (memos.isEmpty()) {
            // 해당 날짜에 메모가 없으면 새로 저장
            Memo newMemo = new Memo(date, newContent);
            memoRepository.save(newMemo);
        } else {
            // 해당 날짜에 메모가 있으면 내용 추가
            Memo existingMemo = memos.get(0);
            existingMemo.setContent(existingMemo.getContent() + "\n" + newContent);
            memoRepository.save(existingMemo);
        }
    }

    // 메모 업데이트 메서드 (ID 기준)
    public LocalDate updateMemo(Long id, String content) {
        Optional<Memo> optionalMemo = memoRepository.findById(id);
        if (optionalMemo.isPresent()) {
            Memo memo = optionalMemo.get();
            memo.setContent(content);
            memoRepository.save(memo);
            return memo.getDate();
        }
        return null;  // 메모가 없을 경우 null 반환
    }
}
