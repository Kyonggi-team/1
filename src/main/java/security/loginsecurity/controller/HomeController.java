package security.loginsecurity.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import security.loginsecurity.exercise.ExerciseService;
import security.loginsecurity.home.service.CalendarService;
import security.loginsecurity.member.Member;
import security.loginsecurity.repository.MemberRepository;

import java.time.LocalDate;


@Controller
public class HomeController {

    private final CalendarService calendarService;
    private final MemberRepository memberRepository;
    private final ExerciseService exerciseService;

    @Autowired
    public HomeController(CalendarService calendarService, MemberRepository memberRepository, ExerciseService exerciseService) {
        this.calendarService = calendarService;
        this.memberRepository = memberRepository;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(Model model, Authentication authentication) {

        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        model.addAttribute("exercise", exerciseService.getExerciseToday(member));
        model.addAttribute("goalToday", exerciseService.getGoalToday(member));

        // home.html 띄우기
        return "home";
    }

    @GetMapping("/memo")
    public String showMemo(@RequestParam(value = "date", required = false) String date, Model model) {
        if (date == null) {
            date = LocalDate.now().toString();  // 파라미터가 없는 경우 오늘 날짜를 문자열로 변환하여 사용
        }
        model.addAttribute("date", date);
        return "redirect:/memo/memo?date=" + date;  // MemoController의 경로로 리다이렉트
    }

    @GetMapping("/aiDiary")
    public String redirectToAiDiary() {
        return "redirect:/aiDiary.html";
    }
}
