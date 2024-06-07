package security.loginsecurity.home.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import security.loginsecurity.home.domain.entity.Calendar;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findByDate(LocalDate date);
}
