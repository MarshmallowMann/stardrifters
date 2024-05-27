package com.group5.stardrifters.utils;

import java.io.Serializable;


public class ScoreMessage extends Message {
    private static final long serialVersionUID = 1L;
    private int score;
    private String name;

    public ScoreMessage(int score, String name) {
        this.score = score;
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}