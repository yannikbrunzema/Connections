package com.example.demo.Util;

import com.example.demo.Models.Puzzle;

import java.net.URL;
import java.security.SecureRandom;

// TODO: Test utils
public final class Utilities
{
    public static String getResourceFilePath(String filename)
    {
        ClassLoader classLoader = Utilities.class.getClassLoader();
        URL resource = classLoader.getResource(filename);

        if (resource == null)
            throw new IllegalArgumentException("Resource not found: " + filename);

        return resource.getFile();
    }

    public static Puzzle loadPuzzle(String filename)
    {
        return PuzzleReader.read(getResourceFilePath(filename));
    }
}

