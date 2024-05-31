package security.loginsecurity.memo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public void saveMood(MoodDto moodDto) {
        Mood mood = new Mood(moodDto.getDate(), moodDto.getMood());
        moodRepository.save(mood);
    }

    public List<MoodDto> getMoodsByDate(LocalDate date) {
        List<Mood> moods = moodRepository.findAllByDate(date);
        return moods.stream()
                .map(m -> new MoodDto(m.getId(), m.getDate(), m.getMood()))
                .collect(Collectors.toList());
    }

    public List<MoodDto> getAllMoods() {
        List<Mood> moods = moodRepository.findAll();
        return moods.stream()
                .map(m -> new MoodDto(m.getId(), m.getDate(), m.getMood()))
                .collect(Collectors.toList());
    }

    public void deleteMoodById(Long id) {
        moodRepository.deleteById(id);
    }
}