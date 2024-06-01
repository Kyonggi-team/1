package security.loginsecurity.memo.domain.repository;


import security.loginsecurity.memo.domain.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByMealTypeAndDate(String mealType, LocalDate date);
}