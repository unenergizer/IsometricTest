package com.forgestorm.isometric.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.forgestorm.isometric.IsometricTest;
import com.forgestorm.isometric.util.ScreenResolutions;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Isometric Demo";
		config.width = ScreenResolutions.DESKTOP_800_600.getWidth();
		config.height = ScreenResolutions.DESKTOP_800_600.getHeight();
		new LwjglApplication(new IsometricTest(), config);
	}
}
