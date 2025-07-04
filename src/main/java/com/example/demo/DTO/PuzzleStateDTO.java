package com.example.demo.DTO;

import com.example.demo.Models.Puzzle;
import com.example.demo.Services.PuzzleService;

import java.util.List;

public class PuzzleStateDTO
{
    private int numSolvedCategories;
    private List<String> unSolvedWords;
    private List<Puzzle.Category> currentSolved;
    private boolean puzzleSolved;

    public PuzzleStateDTO(PuzzleService service)
    {
        this.numSolvedCategories = service.getNumSolvedCategories();
        this.unSolvedWords = service.getUnsolvedWords();
        this.currentSolved = service.getCurrentSolved();
        this.puzzleSolved = service.isPuzzleSolved();
    }

    public int getNumSolvedCategories()
    {
        return numSolvedCategories;
    }

    public List<String> getUnSolvedWords()
    {
        return unSolvedWords;
    }

    public List<Puzzle.Category> getCurrentSolved()
    {
        return currentSolved;
    }

    public boolean isPuzzleSolved()
    {
        return puzzleSolved;
    }
}
