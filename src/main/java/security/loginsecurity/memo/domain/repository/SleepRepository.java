package security.loginsecurity.memo.domain.repository;


import security.loginsecurity.memo.domain.entity.Sleep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepRepository extends JpaRepository<Sleep, Long> {
}

