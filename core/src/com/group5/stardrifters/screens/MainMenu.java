package com.group5.stardrifters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.group5.stardrifters.Application;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.group5.stardrifters.managers.GameScreenManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import javax.swing.event.ChangeEvent;

public class MainMenu extends AbstractScreen {
    SpriteBatch batch = new SpriteBatch();
    Texture bg = new Texture("main_menu_bg.png");
    TextButton.TextButtonStyle buttonStyle = new TextButtonStyle();
    Label title;
    Table table;

    public MainMenu(final Application app) {
        super(app);

        table = new Table();
        table.setFillParent(true);
        title = new Label("Star Drifters", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Press_start.fnt")), Color.WHITE));
        title.setFontScale(1.75f);
        buttonStyle.font = new BitmapFont(Gdx.files.internal("Orbitron.fnt"));
        TextButton playButton = new TextButton("Play", buttonStyle);
        playButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        app.gsm.setScreen(GameScreenManager.STATE.CONNECT);
                        app.gsm.disposeScreen(GameScreenManager.STATE.SPLASH);
                    }
                }
        );
        TextButton exitButton = new TextButton("Exit", buttonStyle);
        exitButton.addListener(new ClickListener() {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       // dispose the game properly
                                       app.dispose();
                                       Gdx.app.exit();
                                   }
                               }
        );
        table.add(title);
        table.getCell(title).spaceBottom(150);
        table.row();
        table.add(playButton).right();
        table.getCell(playButton).spaceBottom(15);
        table.row();
        table.add(exitButton).right();
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
        batch.begin();
        batch.draw(bg, 0, 0);
        batch.end();
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
    }
}
