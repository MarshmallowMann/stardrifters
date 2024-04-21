package com.group5.stardrifters;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.group5.stardrifters.managers.GameScreenManager;
import com.group5.stardrifters.utils.ClientProgram;
import com.group5.stardrifters.utils.MyTextInputListener;

import java.io.IOException;


public class Application extends Game {

    public static String APP_TITLE= "Stardrifters";
    public static String APP_VERSION = "0.1";
    public static int APP_WIDTH = 800;
    public static int APP_HEIGHT = 600;
    public static boolean APP_RESIZABLE = false;
    public static boolean APP_VSYNC = true;
    public static int APP_FPS = 60;


    // Game Vars
    public static int V_WIDTH = 800;
    public static int V_HEIGHT = 600;
    public static String playerName;

    // Managers
    public AssetManager assets;
    public GameScreenManager gsm;

    // Batches
    public SpriteBatch batch;
    public ShapeRenderer shapeBatch;

    // Client
    public ClientProgram clientProgram = new ClientProgram();



    Texture bg;

    @Override
    public void create() {

        assets = new AssetManager();
        batch = new SpriteBatch();
        bg = new Texture("galaxy_bg.png");
        shapeBatch = new ShapeRenderer();
        gsm = new GameScreenManager(this);

//    Connect to client
        try {
            clientProgram.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void render() {
        super.render();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();

        }
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        bg.dispose();
        shapeBatch.dispose();
        gsm.dispose();
    }
}
