package com.group5.stardrifters.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Vector;

import static com.group5.stardrifters.utils.B2DConstants.PPM;

public class B2DBodyBuilder {

    public static Body createBox(World world, float X, float Y, float width, float height) {
        Body body;

        // Body
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.fixedRotation = false;
        bDef.position.set(X/PPM, Y/PPM);

        body = world.createBody(bDef);
        // Shape a triangle
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/PPM, height/2/PPM);

        // Create a triangle
//        PolygonShape shape = new PolygonShape();
//        Vector2[] vertices = new Vector2[3];
//        vertices[0] = new Vector2(-width/2/PPM, -height/2/PPM);
//        vertices[1] = new Vector2(width/2/PPM, -height/2/PPM);
//        vertices[2] = new Vector2(0, height/2/PPM);
//        shape.set(vertices);


        // Fixture
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1.0f;
        fDef.restitution = 0.2f;
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

    public static final float PPM = 32f; // Pixels per meter

    public static Body createTriangle(World world, float X, float Y, float width, float height) {
        Body body;

        // Body
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.fixedRotation = false;
        bDef.position.set(X / PPM, Y / PPM);

        body = world.createBody(bDef);

        // Vertices of the triangle (assuming it's an equilateral triangle)
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(0, height / 2 / PPM); // Top vertex
        vertices[1] = new Vector2(-width / 2 / PPM, -height / 2 / PPM); // Bottom left vertex
        vertices[2] = new Vector2(width / 2 / PPM, -height / 2 / PPM); // Bottom right vertex

        // Create a triangle shape using the vertices
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        // Create fixture and attach shape to the body
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1.0f; // Adjust density as needed
        fDef.friction = 0.5f; // Adjust friction as needed
        fDef.restitution = 0.3f; // Adjust restitution (bounce) as needed

        body.createFixture(fDef);

        // Dispose the shape to avoid memory leaks
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

    public static Body createChainShape(World world, Vector2[] verts, boolean isStatic) {
        Body body;

        // Body
        BodyDef bDef = new BodyDef();
        bDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bDef.fixedRotation = true;

        body = world.createBody(bDef);

        // Shape
        ChainShape shape = new ChainShape();
        shape.createChain(verts);

        // Fixture
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1.0f;
        fDef.restitution = 0.5f;
        body.createFixture(fDef);
        // Dispose
        shape.dispose();
        return body;
    }


}
