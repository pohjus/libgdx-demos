package fi.tuni.tiko.ilvesgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class IlvesGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Player player;

	private OrthographicCamera camera;
	private Texture background;

	public final static float WORLD_WIDTH  = 12.80f;
	public final static float WORLD_HEIGHT = 7.20f;

	@Override
	public void create () {
		batch = new SpriteBatch();
		player = new Player();
		background = new Texture(Gdx.files.internal("hakametsa.jpg"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

		update();

		player.draw(batch);
		batch.end();
	}

	public void update() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_UP)) {
			player.jump();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
			player.changeDirection(player.LEFT);
			player.walk();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
			player.changeDirection(player.RIGHT);
			player.walk();
		}
	}

	@Override
	public void dispose() {
		player.dispose();
	}

}
