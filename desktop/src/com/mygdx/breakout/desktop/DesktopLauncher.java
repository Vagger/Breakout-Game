package com.mygdx.breakout.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.breakout.BreakoutGameClass;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = BreakoutGameClass.TITLE + " " + BreakoutGameClass.VERSION;

		config.vSyncEnabled = true;
		config.useGL30 = true;

		new LwjglApplication(new BreakoutGameClass(), config);
	}
}
