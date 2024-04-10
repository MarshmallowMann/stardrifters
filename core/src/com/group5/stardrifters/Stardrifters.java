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
  Body circleBody, rectangleBody; // Box2D bodies

  float revolutionSpeed = 100f; // Speed of revolution in radians per second
  float totalTime = 0f; // Total time elapsed
    float G = 100f;
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
    circleBody.createFixture(circleShape, 1.0f);
    circleShape.dispose();

    // Create the triangle body
     BodyDef rectangleBodyDef = new BodyDef();
     rectangleBodyDef.type = BodyDef.BodyType.DynamicBody;
     rectangleBodyDef.position.set(Gdx.graphics.getWidth() / 2 + 200, Gdx.graphics.getHeight() / 2);
     rectangleBody = world.createBody(rectangleBodyDef);
     PolygonShape rectangleShape = new PolygonShape();
     rectangleShape.setAsBox(16, 16); // Half-width and half-height
     rectangleBody.createFixture(rectangleShape, 1.0f);
     rectangleShape.dispose();
     System.out.println(rectangleBody.getMass());
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

    // Draw the rectangle
    shapeRenderer.setColor(Color.BLUE); // Set rectangle color
    Vector2 position = rectangleBody.getPosition();
    float halfWidth = 16; // Half-width used in setAsBox
    float halfHeight = 16; // Half-height used in setAsBox
    shapeRenderer.rect(position.x - halfWidth, position.y - halfHeight, halfWidth * 2, halfHeight * 2);

    shapeRenderer.end();

    // Increment the total time by the delta time
    totalTime += Gdx.graphics.getDeltaTime();

    Vector2 circlePosition = circleBody.getPosition();
    Vector2 rectanglePosition = rectangleBody.getPosition();
    Vector2 direction = circlePosition.cpy().sub(rectanglePosition);
    float distance = direction.len();

    // Normalize the direction vector
    if (distance > 0) {
        direction.nor();
    }
    // Calculate gravitational force magnitude (inverse square law)
    float forceMagnitude = (G * 15000* rectangleBody.getMass()) / (distance * distance);
    // Apply the gravitational force to the rectangle body
    Vector2 force = direction.scl(forceMagnitude);
    rectangleBody.applyForceToCenter(force, true);
    // Apply y axis linear force so that it orbits the circle
    Vector2 linearForce = new Vector2(0, 70000);
    rectangleBody.applyForceToCenter(linearForce, true);
}



 @Override
 public void dispose () {
   batch.dispose();
   bg.dispose();
   shapeRenderer.dispose();
   world.dispose(); // Dispose the Box2D world
 }
}