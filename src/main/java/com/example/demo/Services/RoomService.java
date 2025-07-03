package com.example.demo.Services;

import com.example.demo.DTO.*;
import com.example.demo.Exceptions.InvalidRoomIdException;
import com.example.demo.Exceptions.PlayerNotFoundException;
import com.example.demo.Exceptions.RoomFullException;
import com.example.demo.Models.Player;
import com.example.demo.Models.Puzzle;
import com.example.demo.Models.Room;
import com.example.demo.Util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService
{
    private final int ROOM_CLEANUP_INTERVAL_MINUTES = 5;
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    @Autowired
    private final PuzzleQueueService puzzleQueueService;

    // injects puzzle bean in PuzzleConfig
    public RoomService(SimpMessagingTemplate messagingTemplate, PuzzleQueueService puzzleQueueService)
    {
        this.messagingTemplate = messagingTemplate;
        this.puzzleQueueService = puzzleQueueService;
    }

    // TODO: Implement more comprehensive room cleanup
    @Scheduled(fixedRate = 1000 * 60 * ROOM_CLEANUP_INTERVAL_MINUTES)
    public void cleanUpRooms()
    {
        for (Room room : rooms.values())
        {
            if(room.getRoomPlayers().isEmpty())
                rooms.remove(room.getId());
        }
    }


    public RoomCreationResult createRoom(Player player)
    {
        var roomId = IDGenerator.generateId();
        var room = new Room(player, this.puzzleQueueService.getNextPuzzle(), roomId);
        rooms.put(roomId, room);
        return new RoomCreationResult(player, roomId, room.getRoomPuzzleService().getPuzzle());
    }

    public Room getRoom(String roomId) throws InvalidRoomIdException
    {
        if(!rooms.containsKey(roomId))
            throw new InvalidRoomIdException("Room ID " + roomId + " does not exist.");
        return rooms.get(roomId);
    }

    public void updateRoomPuzzle(String roomId, String playerId) throws InvalidRoomIdException
    {
        if(!rooms.containsKey(roomId))
            throw new InvalidRoomIdException("Room ID " + roomId + " does not exist.");

        var room = rooms.get(roomId);

        var newPuzzle = this.puzzleQueueService.getNextPuzzle();

        room.setNewPuzzle(playerId, newPuzzle);

        var playerStateDTO = new PlayerStateDTO(room.getRoomPlayers());
        var puzzleStateDTO = new PuzzleStateDTO(room.getRoomPuzzleService());
        var newPuzzleResult = new NewPuzzleResult(playerStateDTO, puzzleStateDTO);

        messagingTemplate.convertAndSend("/broadcast/room/" + roomId + "puzzle-update", newPuzzleResult);
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

        return new RoomJoinResult(room.getRoomPuzzleService().getPuzzle(), player);
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
