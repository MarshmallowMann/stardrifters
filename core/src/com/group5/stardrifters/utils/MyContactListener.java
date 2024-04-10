package com.group5.stardrifters.utils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.group5.stardrifters.Application;

import java.util.Vector;

import static com.group5.stardrifters.utils.B2DConstants.PPM;
public class MyContactListener implements ContactListener {
    Body box;
    Body circle;

    public MyContactListener(Body box, Body circle) {
        this.box = box;
        this.circle = circle;
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if ((bodyA == box && bodyB == circle) || (bodyA == circle && bodyB == box)) {
            System.out.println("Box and circle have collided!");

            // respawn in a random location in the world
            float x = (float) Math.random() * Application.V_WIDTH;
            float y = (float) Math.random() * Application.V_HEIGHT;
            box.setTransform(x / PPM, y / PPM, 0);

        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
