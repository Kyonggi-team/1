package security.loginsecurity.exercise;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import security.loginsecurity.member.Member;

import java.time.LocalDate;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
    public Exercise getExercise(Member member) {
        //member를 통해 exercise 테이블을 반환
        Exercise exercise = exerciseRepository.findById(member.getId())
                .orElseGet(() -> {
                    Exercise newExercise = Exercise.builder()
                            .id(member.getId())
                            .member(member)
                            .exercise(null)
                            .goal(20)
                            .build();
                    return exerciseRepository.save(newExercise); //exercise 테이블이 만들어지지 않았으면 새로 만듦
                });
        return exercise;
    }

    public String getExerciseToday(Member member) {

        Exercise exercise = getExercise(member);

        //데이터베이스에 영어로 저장된 운동을 한글로 변환
        String exerciseToday = exercise.getExercise();
        if (exerciseToday == null) exerciseToday = "--";
        else if (exerciseToday.equals("pushups")) exerciseToday = "팔굽혀펴기";
        else if (exerciseToday.equals("run")) exerciseToday = "달리기";
        else exerciseToday = "--";

        return exerciseToday;
    }

    public int getGoalToday(Member member) {

        Exercise exercise = getExercise(member);

        //현재 날짜와 이번 달의 마지막 날짜를 통해 오늘의 목표 계산
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        int lengthOfMonth = LocalDate.now().lengthOfMonth();

        int goalToday = (int) Math.ceil(1 + Math.max(0, (exercise.getGoal() - 1) * (dayOfMonth - 1)) / (lengthOfMonth - 1));

        return goalToday;
    }

}