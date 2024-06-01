package security.loginsecurity.controller;

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

    @GetMapping
    public String showMemo(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        List<MemoDto> memos = memoService.getMemosByDate(date);
        List<SleepDto> sleeps = sleepService.getAllSleeps();
        String mealType = null;
        List<MealDto> meals = mealService.getMealsByTypeAndDate(mealType, date);
        List<MoodDto> moods = moodService.getMoodsByDate(date);
        model.addAttribute("date", date);
        model.addAttribute("memos", memos);
        model.addAttribute("sleeps", sleeps);
        model.addAttribute("meals", meals);
        model.addAttribute("moods", moods);
        return "memo";
    }

    @GetMapping("/getMoods")
    @ResponseBody
    public List<MoodDto> getMoods() {
        return moodService.getAllMoods();
    }

    @PostMapping("/save")
    public String saveOrUpdateMemo(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("content") String content) {
        memoService.appendMemoContent(date, content);
        return "redirect:/memo/memo?date=" + date;
    }

    @GetMapping("/edit/{id}")
    public String editMemo(@PathVariable("id") Long id, Model model) {
        MemoDto memo = memoService.getMemoById(id);
        List<SleepDto> sleeps = sleepService.getAllSleeps(); // 슬립 정보 가져오기
        String mealType = null;
        List<MealDto> meals = mealService.getMealsByTypeAndDate(mealType, LocalDate.now()); // 밀 정보 가져오기 (임시로 오늘 날짜를 사용)
        List<MoodDto> moods = moodService.getMoodsByDate(memo.getDate()); // 해당 날짜의 기분 정보 가져오기
        if (memo != null) {
            model.addAttribute("memo", memo);
            model.addAttribute("sleeps", sleeps); // 슬립 정보 전달
            model.addAttribute("meals", meals); // 밀 정보 전달
            model.addAttribute("moods", moods); // 기분 정보 전달
            return "editMemo";
        }
        return "redirect:/memo";
    }

    @PostMapping("/update")
    public String updateMemo(@RequestParam("id") Long id, @RequestParam("content") String content) {
        LocalDate updatedDate = memoService.updateMemo(id, content);
        return "redirect:/memo/memo?date=" + updatedDate.toString();
    }

    @PostMapping("/sleep/save")
    public String saveSleepTime(@RequestParam("start") String start, @RequestParam("end") String end) {
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);
        SleepDto sleepDto = new SleepDto(null, startTime, endTime);
        sleepService.saveSleep(sleepDto);
        return "redirect:/memo";
    }

    @PostMapping("/meals/save")
    public String saveMeal(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("mealType") String mealType,
            @RequestParam("mealTime") String mealTime,
            @RequestParam("mealMenus") String mealMenus,
            @RequestParam("mealRating") int mealRating) {
        LocalTime time = LocalTime.parse(mealTime);
        List<String> menus = Arrays.asList(mealMenus.split(","));
        MealDto mealDto = new MealDto(null, mealType, date, time, menus, mealRating);
        mealService.saveMeal(mealDto);
        return "redirect:/memo/memo?date=" + date;
    }

    @PostMapping("/meals/update")
    public String updateMeal(@ModelAttribute("meal") MealDto mealDto) {
        mealService.saveMeal(mealDto);
        return "redirect:/memo/memo?date=" + mealDto.getDate();
    }

    @PostMapping("/sleep/update")
    public String updateSleep(@ModelAttribute("sleep") SleepDto sleepDto) {
        sleepService.saveSleep(sleepDto);
        return "redirect:/memo";
    }

    @PostMapping("/mood/save")
    public String saveMood(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("mood") String mood,
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

}
