package security.loginsecurity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import security.loginsecurity.memo.dto.MealDto;
import security.loginsecurity.memo.dto.MemoDto;
import security.loginsecurity.memo.dto.MoodDto;
import security.loginsecurity.memo.dto.SleepDto;
import security.loginsecurity.memo.service.MealService;
import security.loginsecurity.memo.service.MemoService;
import security.loginsecurity.memo.service.MoodService;
import security.loginsecurity.memo.service.SleepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/memo/memo")
public class MemoController {
    private final MemoService memoService;
    private final SleepService sleepService;
    private final MealService mealService;
    private final MoodService moodService;

    @Autowired
    public MemoController(MemoService memoService, SleepService sleepService, MealService mealService, MoodService moodService) {
        this.memoService = memoService;
        this.sleepService = sleepService;
        this.mealService = mealService;
        this.moodService = moodService;
    }

    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    /*@GetMapping("/edit/{id}")
    public String editMemo(@PathVariable("id") Long id, Model model) {
        MemoDto memo = memoService.getMemoById(id);


        List<MoodDto> moods = moodService.getMoodsByDate(memo.getDate()); // 해당 날짜의 기분 정보 가져오기
        if (memo != null) {


            model.addAttribute("moods", moods); // 기분 정보 전달
            return "editMemo";
        }
        return "redirect:/memo";
    }*/
    @GetMapping
    public String showMemo(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) throws JsonProcessingException {
        String username = getCurrentUsername();

        List<MemoDto> memos = memoService.getMemosByDate(date, username);
        List<SleepDto> sleeps = sleepService.getSleepsByDate(date, username);
        List<MealDto> meals = mealService.getMealsByDate(date, username);
        List<MoodDto> moods = moodService.getMoodsByDate(date);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String mealsJson = mapper.writeValueAsString(meals);

        model.addAttribute("date", date);
        model.addAttribute("memos", memos);
        model.addAttribute("sleeps", sleeps);
        model.addAttribute("meals", meals);
        model.addAttribute("mealsJson", mealsJson);
        model.addAttribute("moods", moods);

        return "memo";
    }

    @GetMapping("/getMoods")
    @ResponseBody
    public List<MoodDto> getMoods() {
        return moodService.getAllMoods();
    }

    @PostMapping("/saveAll")
    public String saveAll(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @RequestParam(value = "id", required = false) Long memoId,
                          @RequestParam(value = "content", required = false) String content,
                          @RequestParam(value = "start", required = false) String start,
                          @RequestParam(value = "end", required = false) String end,
                          @RequestParam(value = "mealType", required = false) String mealType,
                          @RequestParam(value = "mealTime", required = false) String mealTime,
                          @RequestParam(value = "mealMenus", required = false) String mealMenus,
                          @RequestParam(value = "mealRating", required = false) Integer mealRating) {

        String username = getCurrentUsername();

        // Process memo
        if (content != null && !content.isEmpty()) {
            if (memoId != null) {
                memoService.updateMemo(memoId, content);
            } else {
                memoService.appendMemoContent(date, content, username);
            }
        }

        // Process sleep
        if (start != null && end != null && !start.isEmpty() && !end.isEmpty()) {
            LocalTime startTime = LocalTime.parse(start);
            LocalTime endTime = LocalTime.parse(end);
            SleepDto sleepDto = new SleepDto(null, date, startTime, endTime, null);

            List<SleepDto> existingSleeps = sleepService.getSleepsByDate(date, username);
            if (!existingSleeps.isEmpty()) {
                sleepService.deleteSleepsByDate(date, username);
            }

            sleepService.saveSleep(sleepDto, username);
        }

        // Process meal
        if (mealType != null && mealTime != null && mealMenus != null && !mealType.isEmpty() && !mealTime.isEmpty() && !mealMenus.isEmpty() && mealRating != null) {
            LocalTime time = LocalTime.parse(mealTime);
            List<String> menus = Arrays.asList(mealMenus.split(","));
            MealDto mealDto = new MealDto(null, mealType, date, time, menus, mealRating, null);

            List<MealDto> existingMeals = mealService.getMealsByTypeAndDate(mealType, date, username);
            if (!existingMeals.isEmpty()) {
                mealService.deleteMealsByTypeAndDate(mealType, date, username);
            }

            mealService.saveMeal(mealDto, username);
        }

        return "redirect:/memo/memo?date=" + date;
    }

    @PostMapping("/update")
    public String updateMemo(@RequestParam("id") Long id, @RequestParam("content") String content) {
        LocalDate updatedDate = memoService.updateMemo(id, content);
        return "redirect:/memo/memo?date=" + updatedDate.toString();
    }

    @PostMapping("/sleep/save")
    public String saveSleepTime(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                @RequestParam("start") String start, @RequestParam("end") String end) {
        String username = getCurrentUsername();
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);
        SleepDto sleepDto = new SleepDto(null, date, startTime, endTime, null);

        List<SleepDto> existingSleeps = sleepService.getSleepsByDate(date, username);  // 사용자 정보를 반영하는 로직 필요
        if (!existingSleeps.isEmpty()) {
            sleepService.deleteSleepsByDate(date, username);  // 사용자 정보를 반영하는 로직 필요
        }

        sleepService.saveSleep(sleepDto, username);  // 사용자 정보를 반영하는 로직 필요
        return "redirect:/memo/memo?date=" + date;
    }

    @PostMapping("/meals/save")
    public String saveMeal(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                           @RequestParam("mealType") String mealType,
                           @RequestParam("mealTime") String mealTime,
                           @RequestParam("mealMenus") String mealMenus,
                           @RequestParam("mealRating") int mealRating) {
        String username = getCurrentUsername();
        LocalTime time = LocalTime.parse(mealTime);
        List<String> menus = Arrays.asList(mealMenus.split(","));
        MealDto mealDto = new MealDto(null, mealType, date, time, menus, mealRating, null);

        List<MealDto> existingMeals = mealService.getMealsByTypeAndDate(mealType, date, username);
        if (!existingMeals.isEmpty()) {
            mealService.deleteMealsByTypeAndDate(mealType, date, username);
        }

        mealService.saveMeal(mealDto, username);
        return "redirect:/memo/memo?date=" + date;
    }

    @PostMapping("/mood/save")
    public String saveMood(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("static/assets/mood") String mood,
            Model model) {
        // 동일한 날짜에 이미 기분 데이터가 있는지 확인
        List<MoodDto> existingMoods = moodService.getMoodsByDate(date);
        if (!existingMoods.isEmpty()) {
            // 기존 데이터 삭제
            existingMoods.forEach(m -> moodService.deleteMoodById(m.getId()));
            // 새 데이터 저장
            MoodDto moodDto = new MoodDto(null, date, mood);
            moodService.saveMood(moodDto);

            // 기분 업데이트 메시지 설정
            model.addAttribute("date", date);
            model.addAttribute("moodUpdated", true);
            return "memo";
        }

        MoodDto moodDto = new MoodDto(null, date, mood);
        moodService.saveMood(moodDto);

        // 기분 선택 완료 메시지 설정
        model.addAttribute("date", date);
        model.addAttribute("moodSaved", true);
        return "memo";
    }





    /////
    /*@GetMapping("/summary")
    public String showSummary(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                Model model) {
        String username = getCurrentUsername();
        Map<String, Object> evaluation = mealService.evaluateMeals(startDate, endDate, username);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("evaluation", evaluation);

        return "summary";
    }*/

}
