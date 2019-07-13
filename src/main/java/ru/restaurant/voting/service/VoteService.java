package ru.restaurant.voting.service;

import ru.restaurant.voting.model.Vote;
import ru.restaurant.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface VoteService {

    Vote get(int id, int userId) throws NotFoundException;

    UpdatedVote createOrUpdate(int restaurantId, int userId);

    List<Vote> getAll(int userId);

    List<Vote> getVoteIsBetweenDate(int userId, LocalDate startDate, LocalDate endDate);

    class UpdatedVote extends Vote {

        private boolean isCreated;

        private Vote vote;

        public UpdatedVote() {
        }

        public UpdatedVote(Vote vote, boolean isCreated) {
            this();
            this.isCreated = isCreated;
            this.vote = vote;
        }

        public static UpdatedVote getCreated(Vote vote) {
            return new UpdatedVote(vote, true);
        }

        public static UpdatedVote getUpdated(Vote vote) {
            return new UpdatedVote(vote, false);
        }

        public boolean isCreated() {
            return isCreated;
        }

        public Vote getVote() {
            return vote;
        }


    }
}
