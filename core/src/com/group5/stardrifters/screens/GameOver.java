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
import com.group5.stardrifters.objects.Box;
import com.group5.stardrifters.utils.ClientProgram;

import javax.swing.*;
import java.util.*;

public class GameOver extends AbstractScreen {
    private static final TreeMap<String, Integer> leaderBoard = ClientProgram.leaderBoard;

    public GameOver(final Application app) {
        super(app);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOver = new Label("GAME OVER", font);
        Label playAgain = new Label("Click anywhere to exit", font);

        table.add(gameOver).expandX();
        table.row();
        for (Map.Entry<String, Integer> entry : leaderBoard.entrySet()) {
            System.out.println("pass");
            table.add(new Label(entry.getKey() + ": " + entry.getValue(), font)).expandX().padTop(10f);
            table.row();
        }
        table.add(playAgain).expandX().padTop(10f);

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
        if(Gdx.input.justTouched()){
            app.dispose();
            Gdx.app.exit();
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

    public static class ScoreComparator implements Comparator<Box> {

        @Override
        public int compare(Box o1, Box o2) {
            return o2.score - o1.score;
        }
    }
}
