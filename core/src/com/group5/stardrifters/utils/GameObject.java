package com.group5.stardrifters.utils;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class GameObject implements Serializable {
    private static final long serialVersionUID = 1L;

    // The position, velocity, rotation, and angular velocity for this game object
    private Vector2 position;
    private Vector2 velocity;
    private float rotation;
    private float angularVelocity;
    private String objectName;

    // Other properties, such as health, score, etc.

    public GameObject(Vector2 position, Vector2 velocity, float rotation, float angularVelocity, String objectName) {
        this.position = position;
        this.velocity = velocity;
        this.rotation = rotation;
        this.angularVelocity = angularVelocity;
        this.objectName = objectName;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getRotation() {
        return rotation;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public String getObjectName() {
        return objectName;
    }

    // Other methods for interacting with the game object
}