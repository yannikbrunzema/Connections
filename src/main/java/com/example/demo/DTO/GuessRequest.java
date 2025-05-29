package com.example.demo.DTO;

import com.example.demo.Models.Player;

public class GuessRequest
{
    private Player player;
    private String guess;

    public GuessRequest(Player player, String guess)
    {
        this.player = player;
        this.guess = guess;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public String getGuess()
    {
        return this.guess;
    }
}
