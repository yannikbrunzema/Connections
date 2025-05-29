
package com.example.demo.Models;

import java.util.*;

public class Puzzle
{
    private Difficulty difficulty;
    private Map<String, List<String>> categories;

    public enum Difficulty
    {
        EASY,
        MEDIUM,
        HARD
    }

    // No args constructor for json lib
    public Puzzle() {}

    // Setter method, called by JSON obj mapper
    public void setDifficulty(String difficulty)
    {
        if (difficulty.equalsIgnoreCase("easy"))
            this.difficulty = Difficulty.EASY;
        else if (difficulty.equalsIgnoreCase("medium"))
            this.difficulty = Difficulty.MEDIUM;
        else if (difficulty.equalsIgnoreCase("hard"))
            this.difficulty = Difficulty.HARD;
    }

    // Setter method, called by JSON obj mapper
    public void setCategories(Map<String, List<String>> categories)
    {
        this.categories = categories;
    }

    public Map<String, List<String>> getCategories()
    {
        return this.categories;
    }

    public Difficulty getDifficulty()
    {
        return this.difficulty;
    }
}
