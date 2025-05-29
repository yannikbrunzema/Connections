package com.example.demo.DTO;

import com.example.demo.Models.Player;
import com.example.demo.Models.Puzzle;

public class RoomCreationResult
{
    private final String roomId;
    private final Puzzle puzzle;
    private final Player player;

    public RoomCreationResult(Player player, String roomId, Puzzle puzzle)
    {
        this.roomId = roomId;
        this.puzzle = puzzle;
        this.player = player;
    }

    public String getRoomId() { return roomId; }
    public Puzzle getPuzzle() { return puzzle; }
    public Player getPlayer() { return player; }
}
