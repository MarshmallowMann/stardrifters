package com.group5.stardrifters;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Stardrifters extends ApplicationAdapter {
  SpriteBatch batch;
  Texture bg;
  ShapeRenderer shapeRenderer;
  OrthographicCamera camera;

  World world; // Box2D world
  Body circleBody, triangleBody; // Box2D bodies

  float revolutionSpeed = 1f; // Speed of revolution in radians per second
  float totalTime = 0f; // Total time elapsed

 @Override
 public void create () {
    batch = new SpriteBatch();
    bg = new Texture("galaxy_bg.png");
    shapeRenderer = new ShapeRenderer();

    camera = new OrthographicCamera();
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    world = new World(new Vector2(0, 0), true); // Initialize the Box2D world

    // Create the circle body
    BodyDef circleBodyDef = new BodyDef();
    circleBodyDef.type = BodyDef.BodyType.StaticBody;
    circleBodyDef.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    circleBody = world.createBody(circleBodyDef);
    CircleShape circleShape = new CircleShape();
    circleShape.setRadius(24);
    circleBody.createFixture(circleShape, 0.0f);
    circleShape.dispose();

    // Create the triangle body
    BodyDef triangleBodyDef = new BodyDef();
    triangleBodyDef.type = BodyDef.BodyType.DynamicBody;
    triangleBodyDef.position.set(Gdx.graphics.getWidth() / 2 + 64, Gdx.graphics.getHeight() / 2);
    triangleBody = world.createBody(triangleBodyDef);
    PolygonShape triangleShape = new PolygonShape();
    triangleShape.set(new float[] {-8, -8, 8, -8, 0, 8});
    triangleBody.createFixture(triangleShape, 0.0f);
    triangleShape.dispose();
 }

@Override
public void render () {
    world.step(Gdx.graphics.getDeltaTime(), 6, 2); // Step the Box2D physics simulation

    camera.update();
    batch.setProjectionMatrix(camera.combined);

    batch.begin();
    batch.draw(bg, 0, 0);
    batch.end();

    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(Color.RED);
    shapeRenderer.circle(circleBody.getPosition().x, circleBody.getPosition().y, 24);
    shapeRenderer.end();

    // Increment the total time by the delta time
    totalTime += Gdx.graphics.getDeltaTime();

    // Calculate the new position of the triangle
    float angle = (totalTime * revolutionSpeed) % 360;
    float newX = circleBody.getPosition().x + 64 * (float)Math.cos(angle);
    float newY = circleBody.getPosition().y + 64 * (float)Math.sin(angle);

    // Calculate the direction of the path
    Vector2 direction = new Vector2(newX, newY).sub(triangleBody.getPosition()).nor();

    // Apply a force in the direction of the path
    float forceMagnitude = 1000; // Adjust this value as needed
    triangleBody.applyForceToCenter(direction.scl(forceMagnitude), true);

    // Set the rotation of the triangle body
    Vector2 velocity = triangleBody.getLinearVelocity();
    System.out.println(velocity);
    float rotationAngle = (float)Math.atan2(velocity.y, velocity.x);
    triangleBody.setTransform(triangleBody.getPosition(), rotationAngle);

    shapeRenderer.begin(ShapeType.Line);
    shapeRenderer.setColor(Color.GREEN);
    shapeRenderer.triangle(triangleBody.getPosition().x, triangleBody.getPosition().y,
                           triangleBody.getPosition().x + 16, triangleBody.getPosition().y,
                           triangleBody.getPosition().x + 8, triangleBody.getPosition().y + 16);
    shapeRenderer.end();
}

 @Override
 public void dispose () {
   batch.dispose();
   bg.dispose();
   shapeRenderer.dispose();
   world.dispose(); // Dispose the Box2D world
 }
}