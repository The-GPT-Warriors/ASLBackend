package com.nighthawk.spring_portfolio.mvc.leaderboard;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leaderboard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String playerName;

    private int score;
    private int highestStreak;

    public Leaderboard(String playerName, int score, int highestStreak) {
        this.playerName = playerName;
        this.score = score;
        this.highestStreak = highestStreak;
    }
}