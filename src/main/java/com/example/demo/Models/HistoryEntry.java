package com.example.demo.Models;

import java.util.List;

public class HistoryEntry
{
    private boolean correct;
    private List<String> guess;
    private String playerName;

    public HistoryEntry(boolean isCorrect, List<String> guess, String playerName)
    {
        this.correct = isCorrect;
        this.guess = guess;
        this.playerName = playerName;
    }

    public boolean isCorrect()
    {
        return this.correct;
    }

    public List<String> getGuess()
    {
        return this.guess;
    }

    public String getPlayerName()
    {
        return this.playerName;
    }

}
