package com.example.demo.Exceptions;

public class InvalidRoomIdException extends Exception
{
    public InvalidRoomIdException() {}

    public InvalidRoomIdException(String message)
    {
        super(message);
    }
}
