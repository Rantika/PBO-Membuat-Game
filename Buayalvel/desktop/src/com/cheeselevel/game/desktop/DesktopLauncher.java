package com.cheeselevel.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.cheeselevel.game.BuayaGame;

public class DesktopLauncher {
	public static void main (String[] arg) {

		BuayaGame myProgram = new BuayaGame();
		LwjglApplication launcher = new LwjglApplication(
				myProgram );
	}
}
