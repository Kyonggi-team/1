package security.loginsecurity.exercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import security.loginsecurity.member.Member;
import security.loginsecurity.repository.MemberRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class SurveyController {

    private final SurveyResultRepository surveyResultRepository;
    private final ExerciseRepository exerciseRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public SurveyController(SurveyResultRepository surveyResultRepository, ExerciseRepository exerciseRepository, MemberRepository memberRepository) {
        this.surveyResultRepository = surveyResultRepository;
        this.exerciseRepository = exerciseRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/survey")
    public String showSurveyPage() {
        return "survey";
    }

    @PostMapping("/save-survey")
    public String saveSurvey(Authentication authentication,
                             @RequestParam("answer1") int answer1,
                             @RequestParam("answer2") int answer2,
                             @RequestParam("answer3") int answer3,
                             @RequestParam("answer4") int answer4,
                             @RequestParam("answer5") int answer5,
                             @RequestParam("answer6") int answer6,
                             @RequestParam("answer7") int answer7,
                             @RequestParam("answer8") int answer8,
                             @RequestParam("answer9") int answer9,
                             @RequestParam("exercise") String exercise, @RequestParam("goal") int goal) {

        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        Exercise existingExercise = exerciseRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
                );

        //설문조사의 점수를 합산
        int totalScore = answer1 + answer2 + answer3 + answer4 + answer5 + answer6 + answer7 + answer8 + answer9;
        //현재 날짜를 yyyy-MM 형식의 문자열로 표현
        String surveyMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        //새 Survey 엔티티를 생성
        SurveyResult surveyResult = SurveyResult.builder()
                .member(member)
                .totalScore(totalScore)
                .surveyMonth(surveyMonth)
                .build();

        // 새 SurveyResult 객체 저장
        surveyResultRepository.save(surveyResult);


        // 기존 Exercise 엔티티의 속성을 업데이트
        existingExercise.setExercise(exercise);
        existingExercise.setGoal(goal);

        // 업데이트된 Exercise 객체 저장
        exerciseRepository.save(existingExercise);

        // 저장 후에 home으로 리다이렉트
        return "redirect:/home";
    }
}