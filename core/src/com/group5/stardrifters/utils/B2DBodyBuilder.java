package com.group5.stardrifters.utils;

import com.badlogic.gdx.physics.box2d.*;

import static com.group5.stardrifters.utils.B2DConstants.PPM;

public class B2DBodyBuilder {

    public static Body createBox(World world, float X, float Y, float width, float height) {
        Body body;

        // Body
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.fixedRotation = true;
        bDef.position.set(X/PPM, Y/PPM);

        body = world.createBody(bDef);
        // Shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/PPM, height/2/PPM);

        // Fixture
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1.0f;
        body.createFixture(fDef);
        // Dispose
        shape.dispose();
        return body;
    }

    public static Body createCircle(World world, float X, float Y, float radius) {
        Body body;

        // Body
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.fixedRotation = true;
        bDef.position.set(X/PPM, Y/PPM);

        body = world.createBody(bDef);
        // Shape
        CircleShape shape = new CircleShape();
        shape.setRadius(radius/PPM);

        // Fixture
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1.0f;
        body.createFixture(fDef);
        // Dispose
        shape.dispose();
        return body;
    }

    public static Body createBall(World world, float X, float Y, float radius) {
        Body body;

        // Body
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.fixedRotation = true;
        bDef.position.set(X/PPM, Y/PPM);

        body = world.createBody(bDef);

        // Shape
        CircleShape shape = new CircleShape();
        shape.setRadius(radius/PPM);

        // Fixture
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1.0f;
        body.createFixture(fDef);
        // Dispose
        shape.dispose();
        return body;
    }

}
