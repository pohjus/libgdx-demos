package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class Level1 implements Screen {


    BitmapFont comicSans;


    SpriteBatch batch;
    Texture img;

    MyGdxGame host;

    String text = "";



    public Level1(MyGdxGame host) {
        this.host = host;

        img = new Texture("blue-bird-icon.png");
        batch = host.batch;

        comicSans = host.generateFont();


        text = host.getLevelText("level1");

    }

    @Override
    public void render (float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        // asetaan pikselikamera
        comicSans.draw(batch, text, 50, 50);

        // asetaan metrikamera
        if(Gdx.input.isTouched()) {
            host.setScreen(new Level2(host));
        }
        batch.end();
    }

    @Override
    public void show() {
        String name = host.open("name");
        Gdx.app.log("TAG", name);
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        host.save("Mickey Mouse");
    }

    @Override
    public void dispose () {
        img.dispose();
        comicSans.dispose();
    }
}

