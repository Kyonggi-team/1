package security.loginsecurity.memo.domain.repository;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import security.loginsecurity.memo.domain.entity.Sleep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface SleepRepository extends JpaRepository<Sleep, Long> {
    @Query("SELECT s FROM Sleep s WHERE s.date = :date AND s.member.id = :memberId")
    List<Sleep> findByDate(@Param("date") LocalDate date, @Param("memberId") Long memberId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Sleep s WHERE s.date = :date AND s.member.id = :memberId")
    void deleteByDate(@Param("date") LocalDate date, @Param("memberId") Long memberId);
}

