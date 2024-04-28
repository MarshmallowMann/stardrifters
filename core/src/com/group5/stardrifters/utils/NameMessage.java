package com.group5.stardrifters.utils;

public class NameMessage extends Message {
    private static final long serialVersionUID = 1L;
    private String name;

    public NameMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
