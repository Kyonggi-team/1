package security.loginsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import security.loginsecurity.home.dto.CalendarDto;
import security.loginsecurity.home.service.CalendarService;
import security.loginsecurity.memo.dto.MoodDto;
import security.loginsecurity.memo.service.MoodService;

import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final MoodService moodService;

    @Autowired
    public CalendarController(CalendarService calendarService, MoodService moodService) {
        this.calendarService = calendarService;
        this.moodService = moodService;
    }

    @GetMapping("/events")
    @ResponseBody
    public List<CalendarDto> getAllEvents() {
        return calendarService.getAllEvents();
    }

    @GetMapping("/moods")
    @ResponseBody
    public List<MoodDto> getAllMoods() {
        return moodService.getAllMoods();
    }
}
