package com.nighthawk.spring_portfolio.mvc.leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardApiController {

    @Autowired
    private LeaderboardJpaRepository repository;

    @GetMapping("/")
    public ResponseEntity<List<Leaderboard>> getLeaderboard() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/addScore/{id}/{score}")
    public ResponseEntity<Leaderboard> addScore(@PathVariable long id, @PathVariable int score) {
        Optional<Leaderboard> optional = repository.findById(id);
        if (optional.isPresent()) {
            Leaderboard leaderboard = optional.get();
            leaderboard.setScore(leaderboard.getScore() + score);
            repository.save(leaderboard);
            return new ResponseEntity<>(leaderboard, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Leaderboard> deleteLeaderboard(@PathVariable long id) {
        Optional<Leaderboard> optional = repository.findById(id);
        if (optional.isPresent()) {
            Leaderboard leaderboard = optional.get();
            repository.deleteById(id);
            return new ResponseEntity<>(leaderboard, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/post/{leaderboard}/{score}")
    public ResponseEntity<Leaderboard> postLeaderboard(@PathVariable String leaderboard, @PathVariable int score) {
        // A person object WITHOUT ID will create a new record with default roles as student
        Leaderboard leaderboardrepo = new Leaderboard(null, leaderboard, score);
        repository.save(leaderboardrepo);
        return new ResponseEntity<>(leaderboardrepo, HttpStatus.OK);
    }
}