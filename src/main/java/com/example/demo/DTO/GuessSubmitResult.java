package com.example.demo.DTO;

import com.example.demo.Models.HistoryEntry;
import com.example.demo.Models.Player;

import java.util.List;

public class GuessSubmitResult
{
    private Player guessSubmitter;
    private List<String> guess;
    private boolean correct;
    private HistoryEntry historyEntry;
    private PuzzleStateDTO updatedPuzzleState;
    private PlayerStateDTO playerState;

    public GuessSubmitResult(PlayerStateDTO playerStateDTO, PuzzleStateDTO puzzleState, Player submitter,
                             List<String> guess, boolean correct, HistoryEntry history)
    {
        this.updatedPuzzleState = puzzleState;
        this.guessSubmitter = submitter;
        this.guess = guess;
        this.correct = correct;
        this.historyEntry = history;
        this.playerState = playerStateDTO;
    }

    public Player getGuessSubmitter() { return guessSubmitter; }
    public List<String> getGuess() { return guess; }
    public boolean isCorrect() { return correct; }
    public HistoryEntry getHistory() { return historyEntry; }
    public PuzzleStateDTO getUpdatedPuzzleState() { return updatedPuzzleState; }
    public PlayerStateDTO getPlayerState() { return playerState; }
}
