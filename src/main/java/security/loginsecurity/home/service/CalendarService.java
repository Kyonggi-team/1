package security.loginsecurity.home.service;

import security.loginsecurity.home.domain.entity.Calendar;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.loginsecurity.home.domain.repository.CalendarRepository;
import security.loginsecurity.home.dto.CalendarDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public Optional<CalendarDto> getEventByDate(LocalDate date) {
        Optional<Calendar> event = calendarRepository.findByDate(date);
        return event.map(e -> new CalendarDto(e.getDate(), e.getDescription()));
    }
    //mood관련 calendar나타내는 service추가
    public List<CalendarDto> getAllEvents() {
        List<Calendar> events = calendarRepository.findAll();
        return events.stream()
                .map(event -> new CalendarDto(event.getDate(), event.getDescription()))
                .collect(Collectors.toList());
    }
}
