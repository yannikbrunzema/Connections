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

    public List<String> getActiveRoomPlayers()
    {
        List<String> list = new ArrayList<>();
        for (Player player : players)
        {
            if (player.getGuessesRemaining() > 0)
            {
                list.add(player.getName());
            }
        }
        return list;
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


    private void OnActivePlayerRemoved(Player player)
    {
        int guessesRemaining = player.getGuessesRemaining();
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
