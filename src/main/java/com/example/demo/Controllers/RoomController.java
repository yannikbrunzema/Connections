package com.example.demo.Controllers;

import com.example.demo.DTO.JoinRoomRequest;
import com.example.demo.DTO.LeaveRoomRequest;
import com.example.demo.DTO.RoomJoinResult;
import com.example.demo.Exceptions.InvalidRoomIdException;
import com.example.demo.Exceptions.PlayerNotFoundException;
import com.example.demo.Exceptions.RoomFullException;
import com.example.demo.Models.Player;
import com.example.demo.DTO.RoomCreationResult;
import com.example.demo.Services.PuzzleService;
import com.example.demo.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController
{
    @Autowired
    private RoomService roomService;

    @PostMapping("/create-room")
    public RoomCreationResult createRoom(@RequestBody Player player)
    {
        return roomService.createRoom(player);
    }

    @PostMapping("/join-room")
    public ResponseEntity<?> joinRoom(@RequestBody JoinRoomRequest request)
    {
        try
        {
            var response = roomService.joinRoom(request.getPlayerToJoin(), request.getRoomId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (InvalidRoomIdException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (RoomFullException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("leave-room")
    public ResponseEntity<?> leaveRoom(@RequestBody LeaveRoomRequest request)
    {
        try
        {
            var response = roomService.leaveRoom(request.getPlayerUID(), request.getRoomId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (InvalidRoomIdException | PlayerNotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-room-members")
    public ResponseEntity<List<Player>> getRoomPlayers(@RequestParam String roomId)
    {
        try
        {
           List<Player> players =  this.roomService.getRoomPlayers(roomId);
           return ResponseEntity.ok(players);
        }
        catch (InvalidRoomIdException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-room-puzzle")
    public ResponseEntity<PuzzleService> getRoomPuzzleState(@RequestParam String roomId)
    {
        try
        {
            var room = roomService.getRoom(roomId);
            var puzzleService = room.getRoomPuzzleService();
            return ResponseEntity.ok(puzzleService);
        }
        catch (InvalidRoomIdException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
