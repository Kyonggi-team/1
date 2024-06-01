package security.loginsecurity.home.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import security.loginsecurity.home.domain.entity.Calendar;

import java.time.LocalDate;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findByDate(LocalDate date);
}
