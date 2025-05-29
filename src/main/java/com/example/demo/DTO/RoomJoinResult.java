package com.example.demo.DTO;

import com.example.demo.Models.Player;
import com.example.demo.Models.Puzzle;

import java.util.List;

public class RoomJoinResult
{

    private final Puzzle puzzle;
    private final Player player;

    public RoomJoinResult(Puzzle puzzle, Player player)
    {
        this.puzzle = puzzle;
        this.player = player;
    }


    public Puzzle getPuzzle()
    {
        return this.puzzle;
    }

    public Player getPlayer()
    {
        return this.player;
    }
}
