
package com.example.demo.Models;

import java.util.List;
import java.util.Objects;

public class Puzzle
{
    private Difficulty difficulty;
    private List<Category> categories;

    public enum Difficulty
    {
        EASY,
        MEDIUM,
        HARD
    }

    public static class Category
    {
        private String name;
        private String color;
        private List<String> items;

        // No-args constructor for JSON deserialization
        public Category() {}

        public Category(String name, String color, List<String> items)
        {
            this.name = name;
            this.color = color;
            this.items = items;
        }

        public String getColor() {
            return color;
        }

        public String getName() { return name; }

        public void setColor(String color) {
            this.color = color;
        }

        public List<String> getItems() { return items; }

        public void setItems(List<String> items) {
            this.items = items;
        }

        public void setName(String name) { this.name = name; }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Category category = (Category) o;
            return Objects.equals(name, category.name) &&
                    Objects.equals(color, category.color) &&
                    Objects.equals(items, category.items);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(name, color, items);
        }


    }

    // No-args constructor for JSON deserialization
    public Puzzle() {}

    // Setter method for difficulty called by JSON obj mapper
    public void setDifficulty(String difficulty)
    {
        if (difficulty.equalsIgnoreCase("easy"))
            this.difficulty = Difficulty.EASY;
        else if (difficulty.equalsIgnoreCase("medium"))
            this.difficulty = Difficulty.MEDIUM;
        else if (difficulty.equalsIgnoreCase("hard"))
            this.difficulty = Difficulty.HARD;
    }

    // Setter method for categories called by JSON obj mapper
    public void setCategories(List<Category> categories)
    {
        this.categories = categories;
    }

    public List<Category> getCategories()
    {
        return this.categories;
    }

    public Difficulty getDifficulty()
    {
        return this.difficulty;
    }
}
