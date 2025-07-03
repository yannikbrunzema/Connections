package com.example.demo.Services;

import com.example.demo.Models.Puzzle;
import com.example.demo.Util.PuzzleReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class PuzzleQueueService
{
    private final Queue<Puzzle> puzzleQueue = new LinkedList<>();

    @Value("classpath:puzzles/*.json")
    private Resource [] puzzleFiles;

    @PostConstruct
    public void loadPuzzles() throws IOException
    {
        if (puzzleFiles != null && puzzleFiles.length > 0)
        {
            List<Resource> fileList = Arrays.asList(puzzleFiles);
            Collections.shuffle(fileList);

            for (Resource resource : fileList)
            {
                File file = resource.getFile();
                Puzzle puzzle = PuzzleReader.read(file.getAbsolutePath());
                puzzleQueue.add(puzzle);
            }
        }

        if (puzzleQueue.isEmpty())
        {
            throw new IllegalStateException("No puzzles loaded into the queue.");
        }
    }


    public synchronized Puzzle getNextPuzzle()
    {
        Puzzle nextPuzzle = puzzleQueue.poll();
        puzzleQueue.offer(nextPuzzle);
        return nextPuzzle;
    }
}