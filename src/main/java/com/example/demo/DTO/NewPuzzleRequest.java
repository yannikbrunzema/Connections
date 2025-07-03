package com.example.demo.DTO;

public class NewPuzzleRequest
{
    private String roomId;
    private String playerId;

    public NewPuzzleRequest(String roomId, String playerId)
    {
        this.roomId = roomId;
        this.playerId = playerId;
    }

    public String getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(String playerId)
    {
        this.playerId = playerId;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }
}
