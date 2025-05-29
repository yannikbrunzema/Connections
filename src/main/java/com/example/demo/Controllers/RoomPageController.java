package com.example.demo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/room")
public class RoomPageController
{
    @GetMapping("/{roomId}")
    public String roomPage(@PathVariable String roomId, Model model)
    {
        model.addAttribute("roomId", roomId);
        return "room"; // maps to room.html under /templates
    }
}
