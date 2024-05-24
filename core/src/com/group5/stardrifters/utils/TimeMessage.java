package com.group5.stardrifters.utils;

public class TimeMessage extends Message {
    private long time;

    public TimeMessage(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}