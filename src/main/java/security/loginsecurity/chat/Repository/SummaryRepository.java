package security.loginsecurity.chat.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.loginsecurity.chat.entity.Summary;
import security.loginsecurity.member.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SummaryRepository extends JpaRepository<Summary, Long> {
    Optional<Summary> findByChatDateAndMember(LocalDate chatDate, Member member);
    List<Summary> findByMember(Member member);
}
