package com.example.demo.DTO;

import com.example.demo.Models.Player;

import java.util.List;

public class GuessSubmitResult
{
    private Player player;
    private List<String> guess;
    private boolean correct;

    public GuessSubmitResult(Player player, List<String> guess, boolean correct)
    {
        this.player = player;
        this.guess = guess;
        this.correct = correct;
    }

    public Player getPlayer() { return player; }
    public List<String> getGuess() { return guess; }
    public boolean isCorrect() { return correct; }
}
