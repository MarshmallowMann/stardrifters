package com.group5.stardrifters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.group5.stardrifters.Application;
import com.group5.stardrifters.managers.GameScreenManager;

import java.io.IOException;


public class ConnectScreen extends AbstractScreen {
    SpriteBatch batch = new SpriteBatch();
    Texture bg = new Texture("galaxy_bg.png");
    private String address, port;

    public ConnectScreen(final Application app) {
        super(app);

        Table table = new Table();
        table.left();
        table.center();
        table.setFillParent(true);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = new BitmapFont(Gdx.files.internal("Orbitron.fnt"));
        textFieldStyle.fontColor = Color.WHITE;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("Orbitron.fnt"));
        labelStyle.fontColor = Color.WHITE;

        Label serverTextFieldLabel  = new Label("Enter server address:", labelStyle);
        Label portTextFieldLabel = new Label("Enter port number:", labelStyle);

        TextField serverTextField = new TextField("", textFieldStyle);
        serverTextField.setMessageText("Type here");
        TextField portTextField = new TextField("", textFieldStyle);
        portTextField.setMessageText("Type here");

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont(Gdx.files.internal("Orbitron.fnt"));
        buttonStyle.fontColor = Color.WHITE;
        TextButton connectButton = new TextButton("Connect", buttonStyle);
        connectButton.addListener(new ClickListener() {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       //    Connect to client
                                       Thread clientThread = new Thread(() -> {
                                           try {
                                               address = serverTextField.getText();
                                               serverTextField.setText("");
                                               System.out.println("Server address: " + address);
                                               port = portTextField.getText();
                                               portTextField.setText("");
                                               System.out.println("Port number: " + port);
                                               app.clientProgram.connect(Integer.parseInt(port), address);
                                           } catch (IOException e) {
                                               e.printStackTrace();
                                           }
                                       });
                                       clientThread.start();
                                       app.gsm.setScreen(GameScreenManager.STATE.SPLASH);
                                   }
                               }
        );

        table.add(serverTextFieldLabel).left().expandX().padLeft(50f);
        table.add(serverTextField).left().expandX().fillX();
        table.row();
        table.add(portTextFieldLabel).left().expandX().padTop(10f).padLeft(50f);
        table.add(portTextField).left().expandX().fillX();
        table.row();
        table.add(connectButton).expandX().left().padTop(10f).padLeft(50f);

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
        batch.begin();
        batch.draw(bg, 0, 0);
        batch.end();
        stage.draw();
        Gdx.input.setInputProcessor(stage);
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
