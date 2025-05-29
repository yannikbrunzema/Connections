package com.example.demo.Config;

import com.example.demo.Models.Puzzle;
import com.example.demo.Util.PuzzleReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

@Configuration
public class PuzzleConfig
{
    @Value("${puzzle.file}")
    private Resource puzzleResource;

    @Bean
    public Puzzle puzzle() throws IOException
    {
        File puzzleFile = puzzleResource.getFile();  // throws IOException if not found
        return PuzzleReader.read(puzzleFile.getAbsolutePath());
    }
}
