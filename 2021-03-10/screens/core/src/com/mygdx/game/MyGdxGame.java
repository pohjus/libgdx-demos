package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

// Extends Game!
public class MyGdxGame extends Game {
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();

		// This is new method that you can use!
		setScreen(new Level1(this));
	}

	public BitmapFont generateFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("COMIC.TTF"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 24;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 3;
		return generator.generateFont(parameter);
	}

	// String text = getLevelText("level1");
	public String getLevelText(String key) {
		// Kysy käyttikseltä millä kielellä mennään
		Locale locale = Locale.getDefault();

		I18NBundle myBundle =
				I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
		return myBundle.get(key);
	}

	public void save(String name) {
		Preferences prefs =
				Gdx.app.getPreferences("MyPreferencesSetti");  // MyPreferencesSetti.xml

		prefs.putString("name", name);                           // <name>Jack</name>
		prefs.flush();
	}

	public String open(String key) {
		Preferences prefs =
				Gdx.app.getPreferences("MyPreferencesSetti");

		String name = prefs.getString("name", "No name stored");

		return name;
	}

	@Override
	public void render () {
		super.render();  // Level1:sen tai Level2:sen render
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
