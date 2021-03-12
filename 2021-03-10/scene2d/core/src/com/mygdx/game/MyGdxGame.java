package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

// Scene2D: lautapeli, korttipeli
//     Level => Stage
//     Sprite => Actor
//
// Scene2D-UI: button, tekstikenttä...
public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	// Stage contains hierarcy of actors
	private Stage stage;
	// We will have player actor in the stage
	private BlueBirdActor playerActor;

	private float width = 8.0f;
	private float height = 4.8f;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		// Creating the stage
		stage = new Stage(new FitViewport(width, height), batch);
		// Creating the actors
		playerActor = new BlueBirdActor();
		// add actors to stage
		stage.addActor(playerActor);
		Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Call act on every actor
		stage.act(Gdx.graphics.getDeltaTime());

		// Call draw on every actor
		stage.draw();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
