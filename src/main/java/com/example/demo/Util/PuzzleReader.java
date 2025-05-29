package com.example.demo.Util;

import com.example.demo.Models.Puzzle;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public final class PuzzleReader
{
    /**
     * Reads a puzzle JSON file and maps it to a {@link Puzzle} object.
     *
     * @param fileName The path to the JSON file to read.
     * @return A {@link Puzzle} object representing the parsed JSON data.
     * @throws RuntimeException if the file cannot be read or parsed.
     */
    public static Puzzle read(final String fileName)
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(fileName), Puzzle.class);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
