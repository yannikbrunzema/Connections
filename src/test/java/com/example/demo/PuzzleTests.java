package com.example.demo;

import com.example.demo.Services.PuzzleService;
import com.example.demo.Util.PuzzleReader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class PuzzleTests
{
    @Test
    void puzzleRead()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        String fileName = Objects.requireNonNull(classLoader.getResource("test_puzzle.json")).getFile();

        var puzzle = PuzzleReader.read(fileName);

        assert(puzzle != null);
        assert(puzzle.getDifficulty() != null);
        assert(puzzle.getCategories() != null);
        assert(puzzle.getCategories().size() == 4);
    }

    @Test
    void checkGuess()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        String fileName = Objects.requireNonNull(classLoader.getResource("test_puzzle.json")).getFile();

        var puzzle = PuzzleReader.read(fileName);
        Map<String, List<String>> categories = puzzle.getCategories();

        var puzzleService = new PuzzleService(puzzle);

        assert(!puzzleService.isPuzzleSolved());
        assert(puzzleService.getNumSolvedCategories() == 0);

        // Check null and check garbage guess
        assert (!puzzleService.submitGuessAndValidate(null));
        assert(!puzzleService.submitGuessAndValidate(new ArrayList<>(Arrays.asList("foo", "bar", "bar", "foo"))));
        assert(puzzleService.getNumSolvedCategories() == 0);

        var randomCorrectGuess = new ArrayList<>(categories.values()).get(new Random().nextInt(categories.size()));

        // Check correct guess
        assert(puzzleService.submitGuessAndValidate(randomCorrectGuess));
        assert(puzzleService.getNumSolvedCategories() == 1);

        // The same guess shouldn't be marked as correct
        assert(!puzzleService.submitGuessAndValidate(randomCorrectGuess));
        assert(puzzleService.getNumSolvedCategories() == 1);

        for (List<String> group : categories.values())
        {
            puzzleService.submitGuessAndValidate(group);
        }

        assert (puzzleService.isPuzzleSolved());
        assert (categories.size() == puzzleService.getNumSolvedCategories());
    }
}
