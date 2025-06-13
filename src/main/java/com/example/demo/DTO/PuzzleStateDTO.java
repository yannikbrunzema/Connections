package com.example.demo.DTO;

import com.example.demo.Models.Puzzle;
import com.example.demo.Services.PuzzleService;

import java.util.List;

public class PuzzleStateDTO
{
    private int numSolvedCategories;
    private List<String> unsolvedWords;
    private List<Puzzle.Category> currentSolved;
    private boolean puzzleSolved;

    public PuzzleStateDTO(PuzzleService service)
    {
        this.numSolvedCategories = service.getNumSolvedCategories();
        this.unsolvedWords = service.getUnsolvedWords();
        this.currentSolved = service.getCurrentSolved();
        this.puzzleSolved = service.isPuzzleSolved();
    }

    public int getNumSolvedCategories()
    {
        return numSolvedCategories;
    }

    public List<String> getUnsolvedWords()
    {
        return unsolvedWords;
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
