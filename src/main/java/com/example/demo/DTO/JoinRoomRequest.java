package com.example.demo.DTO;

import com.example.demo.Models.Player;

public class JoinRoomRequest
{
    private String roomId;
    private Player playerToJoin;

    public JoinRoomRequest(String roomId, Player playerToJoin)
    {
        this.roomId = roomId;
        this.playerToJoin = playerToJoin;
    }

    public Player getPlayerToJoin()
    {
        return this.playerToJoin;
    }

    public String getRoomId()
    {
        return this.roomId;
    }
}
