package com.example.demo.Services;

import com.example.demo.Models.Player;
import com.example.demo.Models.Puzzle;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PuzzleService
{
    private final Puzzle puzzle;
    private final List<Puzzle.Category> currentSolvedCategories = new ArrayList<Puzzle.Category>();

    // Need Puzzle to be a bean to autowire here
    public PuzzleService(Puzzle puzzle)
    {
        this.puzzle = puzzle;
    }

    public Puzzle getPuzzle()
    {
        return this.puzzle;
    }

    public List<String> getUnsolvedWords()
    {
        var result = new ArrayList<String>();
        var categories = puzzle.getCategories();
        for (var category : categories)
        {
            if (!currentSolvedCategories.contains(category))
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

    public List<Puzzle.Category> getCurrentSolved()
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

            guess.sort(String::compareTo);
            correctWords.sort(String::compareTo);

            // TODO: We should probably throw a DuplicateGuessException which we handle on the frontend here.
            // Just going to mark the guess as false for now
            if (guess.equals(correctWords) && !currentSolvedCategories.contains(category))
            {
                this.currentSolvedCategories.add(new Puzzle.Category(category.getName(), category.getColor(), category.getItems()));
                return true;
            }
        }

        // Remove a guess if not correct
        submitter.setGuessesRemaining(submitter.getGuessesRemaining() - 1);
        return false;
    }
}
