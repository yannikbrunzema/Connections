package com.example.demo.DTO;

import com.example.demo.Models.Puzzle;

public class NewPuzzleResult
{
    private PuzzleStateDTO puzzleStateDTO;
    private PlayerStateDTO playerStateDTO;

    public NewPuzzleResult(PlayerStateDTO playerStateDTO, PuzzleStateDTO puzzleStateDTO)
    {
        this.puzzleStateDTO = puzzleStateDTO;
        this.playerStateDTO = playerStateDTO;
    }

    public PuzzleStateDTO getPuzzleStateDTO()
    {
        return puzzleStateDTO;
    }

    public PlayerStateDTO getPlayerStateDTO()
    {
        return playerStateDTO;
    }
}
