package ru.restaurant.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Optional<Vote> findByUserIdAndDate(int userId, LocalDate date);

    List<Vote> findByUserIdOrderByDateDesc(int userId);

    List<Vote> findByUserIdAndDateBetweenOrderByDateDesc(int userId, LocalDate startDate, LocalDate endDate);

}
