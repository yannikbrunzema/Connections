package com.example.demo.DTO;

import com.example.demo.Models.Player;

import java.util.List;

public class GuessRequest
{
    private String playerID;
    private List<String> guess;

    public GuessRequest(String player, List<String> guess)
    {
        this.playerID = player;
        this.guess = guess;
    }

    public String getPlayerID()
    {
        return this.playerID;
    }

    public List<String> getGuess()
    {
        return this.guess;
    }
}
