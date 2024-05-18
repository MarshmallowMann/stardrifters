package com.group5.stardrifters.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.group5.stardrifters.Application;
import com.group5.stardrifters.Stardrifters;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.group5.stardrifters.managers.GameScreenManager;

import javax.swing.*;

public class Splash extends AbstractScreen {
    private float elapsedTime, alpha = 0f;
    Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
    Table table = new Table();
    Label splash = new Label("A game made by Group 5", font);

    public Splash(final Application app) {
        super(app);

        table.center();
        table.setFillParent(true);
        table.add(splash).expandX();
        table.row();

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        if (elapsedTime < 2) {
            alpha = elapsedTime;
        } else if (elapsedTime >= 3 && elapsedTime < 5) {
            alpha = 3 - elapsedTime;
        } else {
            alpha = 0f;
            app.gsm.setScreen(GameScreenManager.STATE.CONNECT);
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        splash.getColor().set(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, alpha);
        stage.draw();
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
}
