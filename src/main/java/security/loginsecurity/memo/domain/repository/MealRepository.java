package security.loginsecurity.memo.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import security.loginsecurity.memo.domain.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/*@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByMealTypeAndDateAndMemberId(String mealType, LocalDate date, Long memberId);
    List<Meal> findByDateAndMemberId(LocalDate date, Long memberId);
    void deleteByMealTypeAndDateAndMemberId(String mealType, LocalDate date, Long memberId);*/

public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("SELECT m FROM Meal m WHERE m.mealType = :mealType AND m.date = :date AND m.member.id = :memberId")
    List<Meal> findByMealTypeAndDateAndMemberId(@Param("mealType") String mealType,@Param("date") LocalDate date, @Param("memberId") Long memberId);

    @Query("SELECT m FROM Meal m WHERE  m.date = :date AND m.member.id = :memberId")
    List<Meal> findByDateAndMemberId(@Param("date") LocalDate date, @Param("memberId") Long memberId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.mealType = :mealType AND m.date = :date AND m.member.id = :memberId")
    void deleteByMealTypeAndDateAndMemberId(@Param("mealType") String mealType, @Param("date") LocalDate date, @Param("memberId") Long memberId);




    //
    @Query("SELECT m FROM Meal m WHERE m.date BETWEEN :startDate AND :endDate AND m.member.id = :memberId")
    List<Meal> findByDateBetweenAndMemberId(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("memberId") Long memberId);
}
