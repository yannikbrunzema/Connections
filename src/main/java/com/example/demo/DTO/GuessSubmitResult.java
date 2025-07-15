package com.example.demo.DTO;

import com.example.demo.Models.HistoryEntry;
import com.example.demo.Models.Player;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GuessSubmitResult
{
    private Player guessSubmitter;
    private List<String> guess;
    private boolean correct;
    private HistoryEntry historyEntry;
    private PuzzleStateDTO updatedPuzzleState;
    private PlayerStateDTO playerState;

    @JsonProperty("isLoss")
    private boolean isLoss;

    @JsonProperty("isWin")
    private boolean isWin;


    public GuessSubmitResult(PlayerStateDTO playerStateDTO, PuzzleStateDTO puzzleState, Player submitter,
                             List<String> guess, boolean correct, HistoryEntry history, boolean loss, boolean win)
    {
        this.updatedPuzzleState = puzzleState;
        this.guessSubmitter = submitter;
        this.guess = guess;
        this.correct = correct;
        this.historyEntry = history;
        this.playerState = playerStateDTO;
        this.isLoss = loss;
        this.isWin = win;
    }

    public Player getGuessSubmitter() { return guessSubmitter; }
    public List<String> getGuess() { return guess; }
    public boolean isCorrect() { return correct; }
    public boolean isLoss() { return isLoss; }
    public boolean isWin() { return isWin; }
    public HistoryEntry getHistory() { return historyEntry; }
    public PuzzleStateDTO getUpdatedPuzzleState() { return updatedPuzzleState; }
    public PlayerStateDTO getPlayerState() { return playerState; }
}
