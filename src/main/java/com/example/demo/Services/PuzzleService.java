package com.example.demo.Services;

import com.example.demo.Models.Puzzle;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PuzzleService
{
    private final Puzzle puzzle;
    private final Map<String, List<String>> currentSolvedCategories = new HashMap<>();

    // Need Puzzle to be a bean to autowire here
    public PuzzleService(Puzzle puzzle)
    {
        this.puzzle = puzzle;
    }

    public Puzzle getPuzzle()
    {
        return this.puzzle;
    }

    public List<String> getAllPuzzleWords()
    {
        var result = new ArrayList<String>();
        for (List<String> words: puzzle.getCategories().values())
        {
            result.addAll(words);
        }
        return result;
    }

    public boolean isPuzzleSolved()
    {
        return currentSolvedCategories.size() == puzzle.getCategories().size();
    }

    public int getNumSolvedCategories()
    {
        return currentSolvedCategories.size();
    }

    public Map<String, List<String>> getCurrentSolved()
    {
        return this.currentSolvedCategories;
    }

    public boolean submitGuessAndValidate(List<String> guess)
    {
        if (guess == null)
            return false;

        // Puzzle is already solved
        if (this.isPuzzleSolved())
            return false;

        // We need 4 guesses
        if (guess.size() != puzzle.getCategories().size())
            return false;

        var categories = puzzle.getCategories();

        for (Map.Entry<String, List<String>> entry : categories.entrySet())
        {
            var correctWords = entry.getValue();
            var category = entry.getKey();

            guess.sort(String::compareTo);
            correctWords.sort(String::compareTo);

            if (guess.equals(correctWords) && !currentSolvedCategories.containsKey(category))
            {
                this.currentSolvedCategories.put(category, guess);
                return true;
            }
        }
        return false;
    }
}
