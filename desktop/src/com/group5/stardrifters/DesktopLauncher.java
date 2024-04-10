package com.group5.stardrifters;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.group5.stardrifters.Stardrifters;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		Application app = new Application();
		config.setForegroundFPS(Application.APP_FPS);
		config.setTitle(Application.APP_TITLE);
		config.setWindowedMode(Application.APP_WIDTH, Application.APP_HEIGHT);
		config.setResizable(Application.APP_RESIZABLE);
		config.useVsync(Application.APP_VSYNC);
		new Lwjgl3Application(app, config);
	}
}
