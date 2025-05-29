package com.example.demo.Exceptions;

public class RoomFullException extends Exception
{
    public RoomFullException() {}

    public RoomFullException(String message)
    {
        super(message);
    }
}
