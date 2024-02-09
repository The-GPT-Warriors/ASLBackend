package com.nighthawk.spring_portfolio.mvc.leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardApiController {

    @Autowired
    private LeaderboardJpaRepository repository;

    @GetMapping("/")
    public ResponseEntity<List<Leaderboard>> getLeaderboard() {
        return new ResponseEntity<>(repository.findAllByOrderByScoreDesc(), HttpStatus.OK);
    }

    @PostMapping("/update/{playerName}/{score}/{streak}")
    public ResponseEntity<Leaderboard> updateScoreAndStreak(@PathVariable String playerName, @PathVariable int score, @PathVariable int streak) {
        List<Leaderboard> leaderboardList = repository.findByPlayerNameIgnoreCase(playerName);
        Leaderboard leaderboard;
        if (leaderboardList.isEmpty()) {
            leaderboard = new Leaderboard(playerName, score, streak);
        } else {
            leaderboard = leaderboardList.get(0);
            leaderboard.setScore(leaderboard.getScore() + score);
            leaderboard.setHighestStreak(Math.max(leaderboard.getHighestStreak(), streak));
        }
        repository.save(leaderboard);
        return new ResponseEntity<>(leaderboard, HttpStatus.OK);
    }
}
