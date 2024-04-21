package com.group5.stardrifters.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.group5.stardrifters.Application;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Color;

public class HUD implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private static Integer score;
    private float timeCount;
    private boolean timeUp;

    Label countdownLabel;
    static Label scoreLabel;

    public HUD(SpriteBatch batch) {
        worldTimer = 180;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(Application.V_WIDTH, Application.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%02d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(countdownLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);

        stage.addActor(table);
    }

    public void update(float delta) {
        timeCount += delta;
        if(timeCount >= 1) {
            if(worldTimer > 0) {
                worldTimer--;
            }
            else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() { stage.dispose(); }

    public boolean isTimeUp() { return timeUp; }
}
