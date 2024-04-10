package com.group5.stardrifters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.group5.stardrifters.Application;
import com.group5.stardrifters.managers.GameScreenManager;
import com.group5.stardrifters.utils.B2DBodyBuilder;

import static com.group5.stardrifters.utils.B2DConstants.PPM;

public class GameScreen extends AbstractScreen {
    OrthographicCamera camera;

    // Box2D
    World world;
    Box2DDebugRenderer b2dr;

    // Box2D body
    Body box;
    Body circle;

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
        circle = B2DBodyBuilder.createCircle(world, camera.viewportWidth/2, camera.viewportHeight/2, 8f);
        box = B2DBodyBuilder.createBox(world, camera.viewportWidth/2+200, camera.viewportHeight/2, 32, 32);

        app.batch.setProjectionMatrix(camera.combined);
        app.shapeBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void update(float delta) {
   world.step(1f /Application.APP_FPS, 6, 2);

    // Calculate the vector from the box to the circle
    Vector2 direction = new Vector2(circle.getPosition()).sub(box.getPosition());

    // Calculate the distance between the box and the circle
    float r = direction.len();

    // Calculate the gravitational force
    float F = (circle.getMass() * box.getMass()) / (r * r);

    // Normalize the direction vector
    direction.nor();

    // Multiply the direction by the gravitational force
    direction.scl(F);

    // Apply the gravitational force to the box
    box.applyForceToCenter(direction, true);

    // Apply a larger gravitational force to the circle
    circle.applyForceToCenter(direction.scl(-2), true);

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
