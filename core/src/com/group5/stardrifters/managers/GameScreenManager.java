package com.group5.stardrifters.managers;

import com.group5.stardrifters.Application;
import com.group5.stardrifters.screens.AbstractScreen;
import com.group5.stardrifters.screens.GameScreen;

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
        setScreen(STATE.GAME);
    }

    private void initGameScreens() {
        // Add game screens here
        this.gameScreens = new HashMap<STATE, AbstractScreen>();
        this.gameScreens.put(STATE.GAME, new GameScreen(app));

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
}
