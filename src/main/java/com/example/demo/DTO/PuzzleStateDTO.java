package com.example.demo.DTO;

import com.example.demo.Models.Puzzle;
import com.example.demo.Services.PuzzleService;

import java.util.List;
import java.util.Map;

public class PuzzleStateDTO
{
    private Puzzle puzzle;
    private int numSolvedCategories;
    private List<String> allPuzzleWords;
    private Map<String, List<String>> currentSolved;
    private boolean puzzleSolved;

    public PuzzleStateDTO(PuzzleService service)
    {
        this.puzzle = service.getPuzzle();
        this.numSolvedCategories = service.getNumSolvedCategories();
        this.allPuzzleWords = service.getAllPuzzleWords();
        this.currentSolved = service.getCurrentSolved();
        this.puzzleSolved = service.isPuzzleSolved();
    }

    public Puzzle getPuzzle()
    {
        return puzzle;
    }

    public int getNumSolvedCategories()
    {
        return numSolvedCategories;
    }

    public List<String> getAllPuzzleWords()
    {
        return allPuzzleWords;
    }

    public Map<String, List<String>> getCurrentSolved()
    {
        return currentSolved;
    }

    public boolean isPuzzleSolved()
    {
        return puzzleSolved;
    }
}
