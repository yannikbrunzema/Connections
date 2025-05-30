package com.example.demo.Models;

import com.example.demo.Util.IDGenerator;
import com.example.demo.Util.Utilities;

public class Player
{
    private String UID;
    private String name;
    private int guessesRemaining = 4; // Default value, overwritten on player add/remove
    private boolean isEliminated;

    public Player(final String name)
    {
        this.name = name;
        this.UID = IDGenerator.generateId();
    }

    public String getName()
    {
        return this.name;
    }

    public String getUID() { return this.UID; }

    public boolean isEliminated()
    {
        return this.isEliminated;
    }

    public int getGuessesRemaining()
    {
        return this.guessesRemaining;
    }

    public void setGuessesRemaining(final int guessesRemaining)
    {
        this.guessesRemaining = guessesRemaining;
        if(this.guessesRemaining == 0)
            this.isEliminated = true;
    }

    public void incrementGuessesRemaining(int increment)
    {
        this.guessesRemaining += increment;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player other = (Player) obj;
        return this.name != null && this.name.equals(other.name) && this.getUID().equals(other.getUID());
    }

    @Override
    public int hashCode()
    {
        return name != null ? name.hashCode() : 0;
    }
}
