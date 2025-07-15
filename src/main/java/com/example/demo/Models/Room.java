package com.example.demo.Models;

import com.example.demo.Exceptions.PlayerNotFoundException;
import com.example.demo.Services.PuzzleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room
{
    private boolean roomStarted = false;

    private static final int MAX_GUESSES = 4;
    private static final int MAX_PLAYERS = 4;
    private String id;
    private PuzzleService roomPuzzleService;
    private Player roomOwner;
    private List<Player> players;
    private List<HistoryEntry> history;

    public Room(Player player, Puzzle puzzle, String id)
    {
        this.id = id;
        this.players = new ArrayList<Player>();
        this.roomPuzzleService = new PuzzleService(puzzle);
        this.roomOwner = player;
        this.players.add(player);
        this.history = new ArrayList<HistoryEntry>();
    }

    public PuzzleService getRoomPuzzleService()
    {
        return this.roomPuzzleService;
    }

    public String getId()
    {
        return this.id;
    }

    public boolean AddPlayer(Player player)
    {
        if (players.size() >= MAX_PLAYERS || roomStarted)
            return false;

        players.add(player);
        this.OnPlayerCountChanged();
        return true;
    }

    private int getTotalGuessesRemaining()
    {
        int count = 0;
        for (Player player : players)
        {
            count += player.getGuessesRemaining();
        }
        return count;
    }

    public boolean hasLost()
    {
        return getTotalGuessesRemaining() == 0;
    }

    public boolean hasWon()
    {
        return this.roomPuzzleService.isPuzzleSolved();
    }

    private void resetRoomState()
    {
        this.OnPlayerCountChanged();
        this.history.clear();
    }

    public boolean setNewPuzzle(String playerId, Puzzle puzzle)
    {
        if (playerId == null || !playerId.equals(this.roomOwner.getUID()))
            return false;

        this.resetRoomState();
        this.roomPuzzleService = new PuzzleService(puzzle);
        return true;
    }

    // TODO: Add logic for starting a room
    public boolean startRoom(String ownerUserID)
    {
        if(roomOwner.getUID().equals(ownerUserID))
            roomStarted = true;
        return roomStarted;
    }

    public boolean isRoomStarted()
    {
        return roomStarted;
    }

    public void removePlayer(String playerUid) throws PlayerNotFoundException
    {
        var player = this.getPlayerFromID(playerUid);
        if (player == null)
            throw new PlayerNotFoundException();
        if (!players.contains(player))
            throw new PlayerNotFoundException("Couldn't find a player with UID: " + player.getUID());

        players.remove(player);
        this.OnActivePlayerRemoved(player);
    }

    public List<Player> getRoomPlayers()
    {
        return this.players;
    }

    public List<HistoryEntry> getHistory()
    {
        return this.history;
    }

    private boolean canSubmitGuess(Player player)
    {
        if(!players.contains(player))
            return false;
        for (Player p : players)
        {
            if(p.equals(player) && p.getGuessesRemaining() > 0)
                return true;
        }
        return false;
    }

    public boolean SubmitGuess(Player player, List<String> guess)
    {
        if(!canSubmitGuess(player))
            return false;

        var result = this.roomPuzzleService.submitGuessAndValidate(guess, player);

        this.history.add(new HistoryEntry(result, guess, player.getName()));

        return result;
    }

    public boolean isLoss()
    {
        for (Player p : players)
        {
            if(p.getGuessesRemaining() >= 0)
                return false;
        }
        return true;
    }

    private void OnActivePlayerRemoved(Player player)
    {
        if (this.players.isEmpty())
            return;

        int guessesRemaining = player.getGuessesRemaining();

        if (player.equals(this.roomOwner))
            this.roomOwner = this.players.get(new Random().nextInt(this.players.size()));

        for (int i = 0; i < guessesRemaining; i++)
        {
            var randomOtherPlayer = this.players.get(new Random().nextInt(this.players.size()));
            randomOtherPlayer.incrementGuessesRemaining(1);
        }
    }

    private void OnPlayerCountChanged()
    {
        // Integer division
        int guessesForEachPlayer = MAX_GUESSES / this.players.size();
        int leftover = MAX_GUESSES % players.size();
        for (Player player : players)
        {
            player.setGuessesRemaining(guessesForEachPlayer);
        }
        if (leftover > 0)
            this.players.get(new Random().nextInt(this.players.size())).incrementGuessesRemaining(leftover);
    }

    public Player getPlayerFromID(String playerId)
    {
        for (Player player : players)
        {
            if(player.getUID().equals(playerId))
                return player;
        }
        return null;
    }
}
