package security.loginsecurity.memo.service;

import security.loginsecurity.memo.domain.entity.Sleep;
import security.loginsecurity.memo.domain.repository.SleepRepository;
import security.loginsecurity.memo.dto.SleepDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SleepService {
    private final SleepRepository sleepRepository;

    @Autowired
    public SleepService(SleepRepository sleepRepository) {
        this.sleepRepository = sleepRepository;
    }

    public SleepDto saveSleep(SleepDto sleepDto) {
        Sleep sleep = new Sleep(sleepDto.getStart(), sleepDto.getEnd());
        sleep = sleepRepository.save(sleep);
        return new SleepDto(sleep.getId(), sleep.getStart(), sleep.getEnd());
    }

    public List<SleepDto> getAllSleeps() {
        return sleepRepository.findAll().stream()
                .map(s -> new SleepDto(s.getId(), s.getStart(), s.getEnd()))
                .collect(Collectors.toList());
    }

    public void updateSleep(SleepDto sleepDto) {
        Sleep sleep = new Sleep();
        sleep.setId(sleepDto.getId());
        sleep.setStart(sleepDto.getStart());
        sleep.setEnd(sleepDto.getEnd());
        sleepRepository.save(sleep);
    }
}