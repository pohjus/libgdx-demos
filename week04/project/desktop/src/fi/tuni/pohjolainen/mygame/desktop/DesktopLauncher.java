package fi.tuni.pohjolainen.mygame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fi.tuni.pohjolainen.mygame.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Gorba Game";
		config.height = 500;
		config.width = 1000;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
