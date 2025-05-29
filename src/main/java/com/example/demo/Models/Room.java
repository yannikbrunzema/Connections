package com.example.demo.Models;

import com.example.demo.Exceptions.PlayerNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room
{
    private static final int MAX_GUESSES = 4;
    public static final int MAX_PLAYERS = 4;
    private String id;
    private List<Player> players;


    public Room(Player player, Puzzle puzzle, String id)
    {
        this.id = id;
        this.players = new ArrayList<Player>();
        this.players.add(player);
    }


    public String getId()
    {
        return this.id;
    }

    public boolean AddPlayer(Player player)
    {
        if (players.size() >= MAX_PLAYERS)
            return false;

        players.add(player);
        this.OnPlayerCountChanged();
        return true;
    }

    public void removePlayer(String playerUid) throws PlayerNotFoundException
    {
        var player = this.getPlayerFromID(playerUid);
        if (player == null)
            throw new PlayerNotFoundException();
        if (!players.contains(player))
            throw new PlayerNotFoundException("Couldn't find a player with UID: " + player.getUID());

        players.remove(player);
        this.OnPlayerCountChanged();
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

    public boolean canSubmitGuess(Player player)
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

    private Player getPlayerFromID(String playerId)
    {
        for (Player player : players)
        {
            if(player.getUID().equals(playerId))
                return player;
        }
        return null;
    }
}
