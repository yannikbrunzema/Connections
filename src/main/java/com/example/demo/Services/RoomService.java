package com.example.demo.Services;

import com.example.demo.DTO.RoomJoinResult;
import com.example.demo.Exceptions.InvalidRoomIdException;
import com.example.demo.Exceptions.PlayerNotFoundException;
import com.example.demo.Exceptions.RoomFullException;
import com.example.demo.Models.Player;
import com.example.demo.Models.Puzzle;
import com.example.demo.Models.Room;
import com.example.demo.DTO.RoomCreationResult;
import com.example.demo.Util.IDGenerator;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService
{
    private SimpMessagingTemplate messagingTemplate;
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Puzzle puzzle;

    // injects puzzle bean in PuzzleConfig
    public RoomService(Puzzle puzzle, SimpMessagingTemplate messagingTemplate)
    {
        this.messagingTemplate = messagingTemplate;
        this.puzzle = puzzle;
    }

    public RoomCreationResult createRoom(Player player)
    {
        var roomId = IDGenerator.generateId();
        var room = new Room(player, puzzle, roomId);
        rooms.put(roomId, room);
        return new RoomCreationResult(player, roomId, puzzle);
    }

    public RoomJoinResult joinRoom(Player player, String roomId) throws InvalidRoomIdException, RoomFullException
    {
        if(!rooms.containsKey(roomId))
            throw new InvalidRoomIdException("Room ID " + roomId + " does not exist.");

        var room = rooms.get(roomId);

        boolean added = room.AddPlayer(player);
        if (!added)
            throw new RoomFullException("Room " + roomId + " is full.");
        else
            messagingTemplate.convertAndSend("/broadcast/room/" + roomId, room.getRoomPlayers());

        return new RoomJoinResult(puzzle, player);
    }

    public List<Player> leaveRoom(String playerUid, String roomId) throws InvalidRoomIdException, PlayerNotFoundException
    {
        if(!rooms.containsKey(roomId))
            throw new InvalidRoomIdException("Room ID " + roomId + " does not exist.");

        var room = rooms.get(roomId);
        room.removePlayer(playerUid);
        messagingTemplate.convertAndSend("/broadcast/room/" + roomId, room.getRoomPlayers());
        return this.getRoomPlayers(roomId);
    }

    public List<Player> getRoomPlayers(String roomId) throws InvalidRoomIdException
    {
        if(!rooms.containsKey(roomId))
            throw new InvalidRoomIdException("Couldn't find players. Room ID " + roomId + " doesn't exist");
        var room = rooms.get(roomId);
        return room.getRoomPlayers();
    }
}
