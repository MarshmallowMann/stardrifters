package com.group5.stardrifters.utils;

public class GameStartMessage extends Message{
    private static final long serialVersionUID = 1L;
    private String text;
    private String name;

    public GameStartMessage(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public String getName() { return name; }
}
