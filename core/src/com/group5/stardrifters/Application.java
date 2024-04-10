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

    // Managers
    public AssetManager assets;
    public GameScreenManager gsm;

    // Batches
    public SpriteBatch batch;
    public ShapeRenderer shapeBatch;


    Texture bg;

    @Override
    public void create() {
        gsm = new GameScreenManager(this);
        assets = new AssetManager();
        batch = new SpriteBatch();
        bg = new Texture("galaxy_bg.png");
        shapeBatch = new ShapeRenderer();

    }

    @Override
    public void render() {
        super.render();

        batch.begin();
        batch.draw(bg, 0, 0);
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();

        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        bg.dispose();
        shapeBatch.dispose();
    }
}
