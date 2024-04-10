package com.group5.stardrifters.utils;
import com.badlogic.gdx.physics.box2d.*;
import com.group5.stardrifters.objects.Box;
import com.group5.stardrifters.objects.Circle;

public class MyContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (checkBoxCircle(fixtureA, fixtureB)) {
            Box box;
            Circle circle;

            if (fixtureA.getBody().getUserData() instanceof Box) {
                box = (Box) fixtureA.getBody().getUserData();
                circle = (Circle) fixtureB.getBody().getUserData();
            } else {
                box = (Box) fixtureB.getBody().getUserData();
                circle = (Circle) fixtureA.getBody().getUserData();
            }

            box.hit();
        }



    }


    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    // Check if the box and circle collided and is instance of Box and Circle
    // If so, print "Box and Circle collided"
    private boolean checkBoxCircle(Fixture fixtureA, Fixture fixtureB) {
        boolean a = fixtureA.getBody().getUserData() instanceof Box;
        boolean b = fixtureB.getBody().getUserData() instanceof Circle;
        return a && b;
    }
}
