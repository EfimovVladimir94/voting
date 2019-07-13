package ru.restaurant.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.restaurant.voting.repository.UserRepository;
import ru.restaurant.voting.repository.VoteRepository;
import ru.restaurant.voting.model.Vote;
import ru.restaurant.voting.repository.RestaurantRepository;
import ru.restaurant.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.restaurant.voting.util.Util.EXPIRED_TIME;
import static ru.restaurant.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    VoteRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MenuService menuService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    public Vote get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.findById(id).filter(v -> v.getUser().getId() == userId), id);
    }

    //After EXPIRED_TIME, a new voice is created, if it exists, then DataIntegrityViolationException at the base index.
    @Transactional
    @Override
    public UpdatedVote createOrUpdate(int restaurantId, int userId) {
        return LocalTime.now().isAfter(EXPIRED_TIME) ? UpdatedVote.getCreated(create(restaurantId, userId)) :
                repository.findByUserIdAndDate(userId, LocalDate.now())
                        .map(v -> {
                            v.setMenu(menuService.get(restaurantRepository.getOne(restaurantId)));
                            return UpdatedVote.getUpdated(v);
                        }).orElseGet(() -> UpdatedVote.getCreated(create(restaurantId, userId)));
    }

    @Override
    public List<Vote> getAll(int userId) {
        return repository.findByUserIdOrderByDateDesc(userId);
    }

    @Override
    public List<Vote> getBetweenDates(int userId, LocalDate startDate, LocalDate endDate) {
        Assert.notNull(startDate, "startDate must not be null");
        Assert.notNull(endDate, "endDate  must not be null");
        return repository.findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
    }

    private Vote create(int restaurantId, int userId) {
        return repository.save(new Vote(LocalDate.now(),
                        menuService.get(restaurantRepository.getOne(restaurantId)), userRepository.getOne(userId)));
    }
}
