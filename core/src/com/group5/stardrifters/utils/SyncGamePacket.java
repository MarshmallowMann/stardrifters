package com.group5.stardrifters.utils;

import com.badlogic.gdx.physics.box2d.Body;

import java.io.Serializable;
import java.util.ArrayList;


public class SyncGamePacket extends Message {
    private static final long serialVersionUID = 1L;
    private ArrayList<GameObject> bodies;
    public SyncGamePacket(ArrayList<GameObject> bodies) {
        this.bodies = bodies;
    }


    public ArrayList<GameObject> getBodies() {
        return bodies;
    }
}