package ru.restaurant.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.restaurant.voting.model.Vote;
import ru.restaurant.voting.service.VoteService;
import ru.restaurant.voting.util.DateTimeUtil;
import ru.restaurant.voting.util.Util;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/rest/votes";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    VoteService service;

    @GetMapping
    public List<Vote> getAll() {
        log.debug("getAll");
        return service.getAll(SecurityUtil.authUserId());
    }

    @GetMapping(value = "/filter")
    public List<Vote> getBetween(
            @RequestParam(value = "start", required = false) LocalDate startDate,
            @RequestParam(value = "end", required = false) LocalDate endDate) {
        log.debug("getBetween dates({} - {})", startDate, endDate);
        return service.getBetweenDates(
                SecurityUtil.authUserId(),
                Util.orElse(startDate, DateTimeUtil.MIN_DATE),
                Util.orElse(endDate, DateTimeUtil.MAX_DATE)
        );
    }

    @GetMapping(value = "/{id}")
    public Vote get(@PathVariable int id) {
        log.debug("get id={}", id);
        return service.get(id, SecurityUtil.authUserId());
    }
}
