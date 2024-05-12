package com.group5.stardrifters.managers;

import com.badlogic.gdx.Game;
import com.group5.stardrifters.Application;
import com.group5.stardrifters.screens.*;

import java.awt.*;
import java.util.HashMap;

public class GameScreenManager {
    public final Application app;

    private HashMap<STATE, AbstractScreen> gameScreens;

    public enum STATE {
        SPLASH,
        MENU,
        GAME,
        PAUSE,
        GAMEOVER
    }

    public GameScreenManager(final Application app) {
        this.app = app;

        initGameScreens();
        setScreen(STATE.SPLASH);
    }

    private void initGameScreens() {
        // Add game screens here
        this.gameScreens = new HashMap<STATE, AbstractScreen>();
        this.gameScreens.put(STATE.SPLASH, new Splash(app));
        this.gameScreens.put(STATE.MENU, new MainMenu(app));
        this.gameScreens.put(STATE.GAME, new GameScreen(app));
        this.gameScreens.put(STATE.GAMEOVER, new GameOver(app));
    }

    public void setScreen(STATE nextScreen) {
        app.setScreen(gameScreens.get(nextScreen));
    }

    public void dispose() {
        for (AbstractScreen screen : gameScreens.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
    }

    public void disposeScreen(STATE screen) {
        gameScreens.get(screen).dispose();
    }

    public void reset() {
        this.gameScreens.remove(STATE.GAME);
        this.gameScreens.put(STATE.GAME, new GameScreen(app));
    }
}
