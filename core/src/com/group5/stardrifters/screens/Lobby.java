package com.group5.stardrifters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.group5.stardrifters.Application;
import com.group5.stardrifters.managers.GameScreenManager;
import com.group5.stardrifters.utils.ClientProgram;

import java.io.IOException;

public class Lobby extends AbstractScreen {
    // private int playerCount = ClientProgram.playerCount;
    private Label waitingLabel, playerCountLabel;
    private Table buttonTable;
    SpriteBatch batch = new SpriteBatch();
    Texture bg = new Texture("galaxy_bg.png");

    public Lobby(final Application app) {
        super(app);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("Orbitron.fnt"));
        labelStyle.fontColor = Color.WHITE;

        waitingLabel = new Label("Waiting for other players (Min. of 4)", labelStyle);
        playerCountLabel = new Label(ClientProgram.playerCount + "/8", labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont(Gdx.files.internal("Orbitron.fnt"));
        buttonStyle.fontColor = Color.WHITE;
        TextButton playButton = new TextButton("Play", buttonStyle);

        buttonTable = new Table();
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    ClientProgram.sendStartGameRequest("StartGame");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonTable.add(playButton).expandX();
        buttonTable.setVisible(true);

        table.add(waitingLabel).expandX();
        table.row();
        table.add(playerCountLabel).expandX().padTop(10f);
        table.row();
        table.add(buttonTable).expandX().padTop(10f);

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta) {
        if (ClientProgram.start) {
            app.gsm.setScreen(GameScreenManager.STATE.GAME);
        }
        playerCountLabel.setText(ClientProgram.playerCount + "/8");
        buttonTable.setVisible(ClientProgram.playerCount >= 4);
        batch.begin();
        batch.draw(bg, 0, 0);
        batch.end();
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
