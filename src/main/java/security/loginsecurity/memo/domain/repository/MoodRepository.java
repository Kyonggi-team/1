package security.loginsecurity.memo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.loginsecurity.memo.domain.entity.Mood;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MoodRepository extends JpaRepository<Mood, Long> {
    List<Mood> findAllByDate(LocalDate date);

}
