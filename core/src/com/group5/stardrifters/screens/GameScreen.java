package com.group5.stardrifters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.group5.stardrifters.Application;
import com.group5.stardrifters.utils.B2DBodyBuilder;
import com.group5.stardrifters.utils.MyContactListener;

import static com.group5.stardrifters.utils.B2DConstants.PPM;

public class GameScreen extends AbstractScreen {
    OrthographicCamera camera;

    // Box2D
    World world;
    Box2DDebugRenderer b2dr;

    // Box2D body
    Body box;
    Body circle;

    // Gravitational constant
    float G = 60f;

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
        circle = B2DBodyBuilder.createCircle(world, camera.viewportWidth/2, camera.viewportHeight/2, 16f);
        box = B2DBodyBuilder.createBox(world, camera.viewportWidth/2+300, camera.viewportHeight/2, 32, 32);
        Vector2 linearForce = new Vector2(0, 1000);
        box.applyForceToCenter(linearForce, true);
        app.batch.setProjectionMatrix(camera.combined);
        app.shapeBatch.setProjectionMatrix(camera.combined);
        //apply random angular velocity
        box.setAngularVelocity(0.2f);
        world.setContactListener(new MyContactListener(box, circle));

    }

    @Override
    public void update(float delta) {
   world.step(1f /Application.APP_FPS, 6, 2);
    Vector2 circlePosition = circle.getPosition();
    Vector2 boxPosition = box.getPosition();
    Vector2 direction = circlePosition.cpy().sub(boxPosition);
    float distance = direction.len();



    // Normalize the direction vector
    if (distance > 0) {
        direction.nor();
    }

    // Calculate gravitational force magnitude (inverse square law)
    float forceMagnitude = (G * 15f* box.getMass()) / (distance * distance);
    // Apply the gravitational force to the rectangle body
    Vector2 force = direction.scl(forceMagnitude);

    box.applyForceToCenter(force, true);
    // Apply y axis linear force so that it orbits the circle


//    On space bar click, apply impulse to the box away from the circle
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        // Calculate impulse direction away from the circle
        Vector2 impulseDirection = boxPosition.cpy().sub(circlePosition);
        impulseDirection.nor();
        // Calculate impulse magnitude
        float impulseMagnitude = 15f;
        // Apply impulse to the box
        box.applyLinearImpulse(impulseDirection.scl(impulseMagnitude), boxPosition, true);
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
}
