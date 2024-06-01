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
}
