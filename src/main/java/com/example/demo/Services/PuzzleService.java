package com.example.demo.Services;

import com.example.demo.Models.Player;
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
        var categories = puzzle.getCategories();
        for (var category : categories)
        {
            result.addAll(category.getItems());
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

    public boolean submitGuessAndValidate(List<String> guess, Player submitter)
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

        for (var category : categories)
        {
            var correctWords = category.getItems();
            var topic = category.getName();

            guess.sort(String::compareTo);
            correctWords.sort(String::compareTo);

            if (guess.equals(correctWords) && !currentSolvedCategories.containsKey(topic))
            {
                this.currentSolvedCategories.put(topic, guess);
                return true;
            }
        }

        // Remove a guess if not correct
        submitter.setGuessesRemaining(submitter.getGuessesRemaining() - 1);
        return false;
    }
}
