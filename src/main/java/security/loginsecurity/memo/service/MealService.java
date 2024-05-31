package security.loginsecurity.memo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import security.loginsecurity.memo.domain.entity.Meal;
import security.loginsecurity.member.Member;
import security.loginsecurity.memo.domain.repository.MealRepository;
import security.loginsecurity.memo.dto.MealDto;
import security.loginsecurity.service.MemberService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final MemberService memberService;

    @Autowired
    public MealService(MealRepository mealRepository, MemberService memberService) {
        this.mealRepository = mealRepository;
        this.memberService = memberService;
    }

    private Member getCurrentMember() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return memberService.findByEmail(userDetails.getUsername());
    }

    public List<MealDto> getMealsByTypeAndDate(String mealType, LocalDate date, String username) {
        Member member = getCurrentMember();
        List<Meal> meals = mealRepository.findByMealTypeAndDateAndMemberId(mealType, date, member.getId());
        return meals.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<MealDto> getMealsByDate(LocalDate date, String username) {
        Member member = getCurrentMember();
        List<Meal> meals = mealRepository.findByDateAndMemberId(date, member.getId());
        return meals.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void saveMeal(MealDto mealDto, String username) {
        Meal meal = convertToEntity(mealDto);
        Member currentMember = getCurrentMember();
        meal.setMember(currentMember);
        mealRepository.save(meal);
    }

    public void updateMeal(MealDto mealDto) {
        Optional<Meal> optionalMeal = mealRepository.findById(mealDto.getId());
        if (optionalMeal.isPresent()) {
            Meal meal = optionalMeal.get();
            meal.setMealType(mealDto.getMealType());
            meal.setDate(mealDto.getDate());
            meal.setTime(mealDto.getTime());
            meal.setMenus(String.join(", ", mealDto.getMenus()));
            meal.setRating(mealDto.getRating());
            mealRepository.save(meal);
        } else {
            throw new RuntimeException("Meal not found with id: " + mealDto.getId());
        }
    }

    private MealDto convertToDto(Meal meal) {
        MealDto dto = new MealDto();
        dto.setId(meal.getId());
        dto.setMealType(meal.getMealType());
        dto.setDate(meal.getDate());
        dto.setTime(meal.getTime());
        dto.setMenus(Arrays.asList(meal.getMenus().split(", ")));
        dto.setRating(meal.getRating());
        dto.setMemberId(meal.getMember().getId());
        return dto;
    }

    private Meal convertToEntity(MealDto mealDto) {
        Meal meal = new Meal();
        meal.setId(mealDto.getId());
        meal.setMealType(mealDto.getMealType());
        meal.setDate(mealDto.getDate());
        meal.setTime(mealDto.getTime());
        meal.setMenus(String.join(", ", mealDto.getMenus()));
        meal.setRating(mealDto.getRating());
        if (mealDto.getMemberId() != null) {
            Member member = memberService.findById(mealDto.getMemberId());
            meal.setMember(member);
        }
        return meal;
    }

    @Transactional
    public void deleteMealsByTypeAndDate(String mealType, LocalDate date, String username) {
        //Member member = memberService.findByEmail(username);
        Member member = getCurrentMember();
        mealRepository.deleteByMealTypeAndDateAndMemberId(mealType, date, member.getId());
    }




    //분산계산
    public Map<String, Object> evaluateMeals(LocalDate startDate, LocalDate endDate, String username) {
        Member member = getCurrentMember();
        List<Meal> meals = mealRepository.findByDateBetweenAndMemberId(startDate, endDate, member.getId());
        Map<String, List<Meal>> mealsByType = meals.stream().collect(Collectors.groupingBy(Meal::getMealType));

        double totalRating = 0;
        double totalVariance = 0;
        int totalMeals = 0;
        double[] dailyMeans = new double[mealsByType.size()];

        int day = 0;
        for (Map.Entry<String, List<Meal>> entry : mealsByType.entrySet()) {
            double dailyTotalTime = 0;
            List<Meal> dailyMeals = entry.getValue();
            for (Meal meal : dailyMeals) {
                dailyTotalTime += meal.getTime().toSecondOfDay() / 60;
                totalRating += meal.getRating();
                totalMeals++;
            }
            double dailyMean = dailyTotalTime / dailyMeals.size();
            dailyMeans[day++] = dailyMean;
        }

        double globalMean = Arrays.stream(dailyMeans).average().orElse(0);

        for (double dailyMean : dailyMeans) {
            totalVariance += Math.pow(dailyMean - globalMean, 2);
        }
        totalVariance /= dailyMeans.length;

        double averageRating = totalRating / totalMeals;
        String timeEvaluation = evaluateTime(totalVariance);

        Map<String, Object> evaluation = new HashMap<>();
        evaluation.put("timeVariance", totalVariance);
        evaluation.put("averageRating", averageRating);
        evaluation.put("timeEvaluation", timeEvaluation);

        // Add meal time variance to the evaluation map
        evaluation.put("mealTimeEvaluation", timeEvaluation); // 이 부분을 수정하여 meal time variance 값을 추가합니다.

        return evaluation;
    }

    private String evaluateTime(double meanVariance) {
        if (meanVariance < 60) {
            return "훌륭해요";
        } else if (meanVariance < 90) {
            return "매우 좋아요";
        } else if (meanVariance < 120) {
            return "좋아요";
        } else if (meanVariance < 150) {
            return "좀 더 노력해요";
        } else {
            return "규칙적인 식사시간을 갖도록 노력해봅시다";
        }
    }



}
