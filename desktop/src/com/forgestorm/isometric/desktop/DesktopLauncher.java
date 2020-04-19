package com.forgestorm.isometric.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.forgestorm.isometric.IsometricTest;
import com.forgestorm.isometric.util.ScreenResolutions;

public class DesktopLauncher {
	public static void main (String[] arg) {
		ScreenResolutions screenResolution = ScreenResolutions.DESKTOP_1280_720;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Isometric Demo";
		config.width = screenResolution.getWidth();
		config.height = screenResolution.getHeight();
		new LwjglApplication(new IsometricTest(screenResolution), config);
	}
}
