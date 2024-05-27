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

    public GameOver(final Application app) {
        super(app);

    }

    public static HashMap<String, Integer> sortByValueDescending(HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                // Descending order by value
                return o2.getValue() - o1.getValue();
            }
        });

        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    @Override
    public void show() {
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOver = new Label("GAME OVER", font);
        Label playAgain = new Label("Click anywhere to exit", font);

        Map<String, Integer> leaderBoard = sortByValueDescending(ClientProgram.leaderBoard);

        table.add(gameOver).expandX();
        table.row();
        for (Map.Entry<String, Integer> entry : leaderBoard.entrySet()) {
            table.add(new Label(entry.getKey() + ": " + entry.getValue(), font)).expandX().padTop(10f);
            table.row();
        }
        table.add(playAgain).expandX().padTop(10f);

        stage.addActor(table);
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
}
