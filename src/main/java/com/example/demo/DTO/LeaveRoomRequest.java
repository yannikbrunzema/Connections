package com.example.demo.DTO;

import com.example.demo.Models.Player;

public class LeaveRoomRequest
{
    private String playerUID;
    private String roomId;

    public LeaveRoomRequest(String playerUID, String roomId)
    {
        this.playerUID = playerUID;
        this.roomId = roomId;
    }

    public String getPlayerUID()
    {
        return this.playerUID;
    }

    public String getRoomId()
    {
        return this.roomId;
    }
}
