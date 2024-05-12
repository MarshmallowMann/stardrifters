package com.group5.stardrifters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.group5.stardrifters.Application;

import java.io.IOException;

public abstract class AbstractScreen implements Screen {

    protected final Application app;

    // Stage for UI
    Stage stage;

    public AbstractScreen(final Application app) {
        this.app = app;
        stage = new Stage();
    }

    public abstract void update(float delta) throws IOException;

    @Override
    public void render(float delta) {
        try {
            update(delta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }


    @Override
    public void dispose() {
        this.stage.dispose();
    }


}
