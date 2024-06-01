package security.loginsecurity.memo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.loginsecurity.member.Member;
import security.loginsecurity.memo.domain.entity.Mood;
import security.loginsecurity.memo.domain.repository.MoodRepository;
import security.loginsecurity.memo.dto.MoodDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoodService {
    private final MoodRepository moodRepository;

    @Autowired
    public MoodService(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    public void saveMood(MoodDto moodDto, Member member) {
        Mood mood = new Mood(moodDto.getDate(), moodDto.getMood());
        mood.setMember(member);
        moodRepository.save(mood);
    }

    public List<MoodDto> getMoodsByDateAndMember(LocalDate date, Long memberId) {
        List<Mood> moods = moodRepository.findAllByDateAndMemberId(date, memberId);
        return moods.stream()
                .map(m -> new MoodDto(m.getId(), m.getDate(), m.getMood()))
                .collect(Collectors.toList());
    }

    public List<MoodDto> getAllMoodsByMember(Long memberId) {
        List<Mood> moods = moodRepository.findAllByMemberId(memberId);
        return moods.stream()
                .map(m -> new MoodDto(m.getId(), m.getDate(), m.getMood()))
                .collect(Collectors.toList());
    }

    public void deleteMoodById(Long id) {
        moodRepository.deleteById(id);
    }
}
