package com.group5.stardrifters.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
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
        world = new World(new Vector2(0, -9.7f), false);
        box = B2DBodyBuilder.createBox(world, camera.viewportWidth/2, camera.viewportHeight/2, 32, 32);
        circle = B2DBodyBuilder.createCircle(world, camera.viewportWidth/2+50, camera.viewportHeight/2+50, 32);


        app.batch.setProjectionMatrix(camera.combined);
        app.shapeBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void update(float delta) {
        world.step(1/Application.APP_FPS, 6, 2);

        stage.act(delta);

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
}
