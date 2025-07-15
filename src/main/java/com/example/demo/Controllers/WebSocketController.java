package com.example.demo.Controllers;

import com.example.demo.DTO.GuessRequest;
import com.example.demo.DTO.GuessSubmitResult;
import com.example.demo.DTO.PlayerStateDTO;
import com.example.demo.DTO.PuzzleStateDTO;
import com.example.demo.Exceptions.InvalidRoomIdException;
import com.example.demo.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController
{
    // Each room contains a puzzle service, access state there
    @Autowired
    private RoomService roomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/room/{roomId}/guess-submit")
    // TODO: Actual error handling
    public void submitGuess(@DestinationVariable String roomId, @Payload GuessRequest request) throws InvalidRoomIdException
    {
        var room = roomService.getRoom(roomId);
        // TODO: Should getPlayerFromID be private and consumed internally?
        var player = room.getPlayerFromID(request.getPlayerID());
        var correct = room.SubmitGuess(player, request.getGuess());
        var history = room.getHistory();
        var result = new GuessSubmitResult(new PlayerStateDTO(room.getRoomPlayers()), new PuzzleStateDTO(room.getRoomPuzzleService()),
                player, request.getGuess(), correct, (history.get(history.size()-1)), room.hasLost(), room.hasWon());
        messagingTemplate.convertAndSend("/broadcast/room/" + roomId + "/submit" , result);
    }
}
