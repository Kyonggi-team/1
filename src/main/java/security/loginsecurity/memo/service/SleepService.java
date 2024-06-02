package security.loginsecurity.memo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import security.loginsecurity.member.Member;
import security.loginsecurity.memo.domain.entity.Sleep;
import security.loginsecurity.memo.domain.repository.SleepRepository;
import security.loginsecurity.memo.dto.SleepDto;
import security.loginsecurity.service.MemberService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SleepService {
    private final SleepRepository sleepRepository;
    private final MemberService memberService;

    @Autowired
    public SleepService(SleepRepository sleepRepository, MemberService memberService) {
        this.sleepRepository = sleepRepository;
        this.memberService = memberService;
    }

    private Member getCurrentMember() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return memberService.findByEmail(userDetails.getUsername());
    }

    public List<SleepDto> getSleepsByDate(LocalDate date, String username) {
        Member member = memberService.findByEmail(username);
        List<Sleep> sleeps = sleepRepository.findByDate(date, member.getId());
        return sleeps.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private SleepDto convertToDto(Sleep sleep) {
        return new SleepDto(
                sleep.getId(),
                sleep.getDate(),
                sleep.getStart(),
                sleep.getEnd(),
                sleep.getMember().getId()
        );
    }

    public void saveSleep(SleepDto sleepDto, String username) {
        Member member = memberService.findByEmail(username);
        Sleep sleep = convertToEntity(sleepDto, member);
        sleepRepository.save(sleep);
    }

    private Sleep convertToEntity(SleepDto sleepDto, Member member) {
        return new Sleep(
                sleepDto.getDate(),
                sleepDto.getStart(),
                sleepDto.getEnd(),
                member
        );
    }

    public List<SleepDto> getAllSleeps() {
        Member member = getCurrentMember();
        return sleepRepository.findAll().stream()
                .filter(s -> s.getMember().getId().equals(member.getId()))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void updateSleep(SleepDto sleepDto) {
        Member member = getCurrentMember();
        Sleep sleep = convertToEntity(sleepDto, member);
        sleep.setId(sleepDto.getId());
        sleepRepository.save(sleep);
    }

    @Transactional
    public void deleteSleepsByDate(LocalDate date, String username) {
        //Member member = memberService.findByEmail(username);
        Member member = getCurrentMember();
        sleepRepository.deleteByDate(date, member.getId());
    }
    //분산계산
    public double calculateVariance(List<SleepDto> sleeps) {
        double mean = sleeps.stream()
                .mapToLong(SleepDto::calculateDuration)
                .average()
                .orElse(0.0);

        double variance = sleeps.stream()
                .mapToDouble(s -> Math.pow(s.calculateDuration() - mean, 2))
                .average()
                .orElse(0.0);

        return variance;
    }

    public String evaluateSleepVariance(double variance) {
        if (variance < 60) {
            return "훌륭해요";
        } else if (variance < 90) {
            return "매우 좋아요";
        } else if (variance < 120) {
            return "좋아요";
        } else if (variance < 150) {
            return "좀 더 노력해요";
        } else {
            return "규칙적인 수면 시간을 갖도록 노력해봅시다";
        }
    }

    public List<SleepDto> getSleepsBetween(LocalDate startDate, LocalDate endDate, String username) {
        Member member = memberService.findByEmail(username);
        List<Sleep> sleeps = sleepRepository.findByDateBetweenAndMemberId(startDate, endDate, member.getId());
        return sleeps.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
