package security.loginsecurity.memo.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import security.loginsecurity.memo.domain.entity.Memo;

import java.time.LocalDate;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    @Query("SELECT m FROM Memo m WHERE m.date = :date AND m.member.id = :memberId")
    List<Memo> findAllByDate(@Param("date") LocalDate date, @Param("memberId") Long memberId);
}
