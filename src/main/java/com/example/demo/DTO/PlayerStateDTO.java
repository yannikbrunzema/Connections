package com.example.demo.DTO;

import com.example.demo.Models.Player;

import java.util.List;

public class PlayerStateDTO
{
    private final List<Player> players;

    public PlayerStateDTO(List<Player> players)
    {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return this.players;
    }
}
