package com.group5.stardrifters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.group5.stardrifters.Application;
import com.group5.stardrifters.objects.Box;
import com.group5.stardrifters.objects.Circle;
import com.group5.stardrifters.objects.Food;
import com.group5.stardrifters.utils.B2DBodyBuilder;
import com.group5.stardrifters.utils.MyContactListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.group5.stardrifters.utils.B2DConstants.PPM;

public class GameScreen extends AbstractScreen {
    OrthographicCamera camera;

    // Box2D
    World world;
    Box2DDebugRenderer b2dr;

    // Box2D body
    // Array of boxes
    ArrayList<Box> boxes = new ArrayList<Box>();
    ArrayList<Food> foods = new ArrayList<Food>();
    Circle circle;

    // Gravitational constant
    float G = 60f;
    boolean shouldMoveDynamicBody = false;

    public GameScreen(final Application app) {
        super(app);

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);
        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void show() {
        world = new World(new Vector2(0, 0f), false);
        createWalls();
//        box = B2DBodyBuilder.createBox(world, camera.viewportWidth/2, camera.viewportHeight/2, 32, 32);
        circle = new Circle(world, camera.viewportWidth/2, camera.viewportHeight/2, 16f, "circle");
        for (int i = 0; i < 2; i++) {
            Box box = new Box(world, camera.viewportWidth/2, camera.viewportHeight/2, 32, 32, "box" + i);
            box.respawn(camera);
            Vector2 linearForce = new Vector2(0, 1000);
            box.body.applyForceToCenter(linearForce, true);
            box.body.setAngularVelocity(0.2f);

            boxes.add(box);
        }

        for (int i = 0; i < 10; i++) {
            Food food = new Food(world, camera.viewportWidth/2, camera.viewportHeight/2, 16, 16);
            food.respawn(camera);
            foods.add(food);
        }

        app.batch.setProjectionMatrix(camera.combined);
        app.shapeBatch.setProjectionMatrix(camera.combined);
        MyContactListener contactListener = new MyContactListener();
        world.setContactListener(contactListener);

    }

    @Override
    public void update(float delta) {
   world.step(1f /Application.APP_FPS, 6, 2);
    Vector2 circlePosition = circle.body.getPosition();

    for (Box box : boxes) {
        Vector2 boxPosition = box.body.getPosition();
        Vector2 direction = circlePosition.cpy().sub(boxPosition);
        float distance = direction.len();
        if (distance > 0) {
            direction.nor();
        }
        if (box.hit) {
            box.respawn(camera);

        }
        float forceMagnitude = (G * 15f* box.body.getMass()) / (distance * distance);
        // Apply the gravitational force to the rectangle body
        Vector2 force = direction.scl(forceMagnitude);

        box.body.applyForceToCenter(force, true);
        // Apply y axis linear force so that it orbits the circle

    }

    for (Food food : foods) {
        if (food.hit) {
            food.respawn(camera);

        }

    }



    // Normalize the direction vector


    // Calculate gravitational force magnitude (inverse square law)

//    On space bar click, apply impulse to the box away from the circle
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        // Calculate impulse direction away from the circle
        Vector2 boxPosition = boxes.get(0).body.getPosition();
        Vector2 impulseDirection = boxPosition.cpy().sub(circlePosition);
        impulseDirection.nor();
        // Calculate impulse magnitude
        float impulseMagnitude = 25f;
        // Apply impulse to the box
        boxes.get(0).body.applyLinearImpulse(impulseDirection.scl(impulseMagnitude), boxPosition, true);
    }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            // Calculate impulse direction away from the circle
            Vector2 boxPosition = boxes.get(1).body.getPosition();
            Vector2 impulseDirection = boxPosition.cpy().sub(circlePosition);
            impulseDirection.nor();
            // Calculate impulse magnitude
            float impulseMagnitude = 15f;
            // Apply impulse to the box
            boxes.get(1).body.applyLinearImpulse(impulseDirection.scl(impulseMagnitude), boxPosition, true);
        }


    stage.act(delta);
    this.camera.update();
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        b2dr.render(world, camera.combined.cpy().scl(PPM));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        b2dr.dispose();
    }

    private void createWalls() {
        Vector2[] verts = new Vector2[5];
        verts[0] = new Vector2(1f / PPM, 0);
        verts[1] = new Vector2(camera.viewportWidth / PPM, 0);
        verts[2] = new Vector2(camera.viewportWidth / PPM, (camera.viewportHeight - 1f) / PPM);
        verts[3] = new Vector2(1f / PPM, (camera.viewportHeight - 1f) / PPM);
        verts[4] = new Vector2(1f / PPM, 0);
        B2DBodyBuilder.createChainShape(world, verts, true);
    }

    public void setShouldMoveDynamicBody(boolean shouldMoveDynamicBody) {
        this.shouldMoveDynamicBody = shouldMoveDynamicBody;
    }

    //function
}
