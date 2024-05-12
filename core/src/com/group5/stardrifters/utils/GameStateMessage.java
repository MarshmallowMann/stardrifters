package com.group5.stardrifters.utils;

import java.io.Serializable;


public class GameStateMessage extends Message {
    private static final long serialVersionUID = 1L;
    private final int PlayerCount;

    public GameStateMessage(int PlayerCount) {
        this.PlayerCount = PlayerCount;
    }


    public int getPlayerCount() {
        return PlayerCount;
    }
}