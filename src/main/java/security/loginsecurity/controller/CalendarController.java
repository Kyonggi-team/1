
package security.loginsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import security.loginsecurity.home.dto.CalendarDto;
import security.loginsecurity.home.service.CalendarService;
import security.loginsecurity.member.Member;
import security.loginsecurity.memo.dto.MoodDto;
import security.loginsecurity.memo.service.MoodService;
import security.loginsecurity.service.MemberService;

import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final MoodService moodService;
    private final MemberService memberService;

    @Autowired
    public CalendarController(CalendarService calendarService, MoodService moodService, MemberService memberService) {
        this.calendarService = calendarService;
        this.moodService = moodService;
        this.memberService = memberService;
    }

    @GetMapping("/events")
    @ResponseBody
    public List<CalendarDto> getAllEvents() {
        return calendarService.getAllEvents();
    }

    @GetMapping("/moods")
    @ResponseBody
    public List<MoodDto> getAllMoods(@AuthenticationPrincipal User user) {
        Member currentMember = memberService.findByEmail(user.getUsername());
        return moodService.getAllMoodsByMember(currentMember.getId());
    }
}
