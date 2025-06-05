package com.example.demo.DTO;

import com.example.demo.Models.Player;

import java.util.List;

public class GuessRequest
{
    private Player player;
    private List<String> guess;

    public GuessRequest(Player player, List<String> guess)
    {
        this.player = player;
        this.guess = guess;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public List<String> getGuess()
    {
        return this.guess;
    }
}
