package fi.tuni.pohjolainen.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture gorbaTexture;
	private Rectangle gorbaRectangle;

	private Texture phoneTexture;
	private Rectangle phoneRectangle;

	private Sound soundEffect;
	private Music backgroundMusic;

	private OrthographicCamera camera;

	final int GORBA_SPEED = 5;

	public static final float WORLD_HEIGHT = 5f;
	public static final float WORLD_WIDTH = 10f;

	@Override
	public void create () {
		gorbaTexture = new Texture(Gdx.files.internal("gorba.png"));
		gorbaRectangle = new Rectangle(
				0,
				0,
				gorbaTexture.getWidth()  / 100.0f,
				gorbaTexture.getHeight() / 100.0f);

		phoneTexture = new Texture(Gdx.files.internal("nokia.png"));
		phoneRectangle = new Rectangle(
				WORLD_HEIGHT / 2f,
				WORLD_HEIGHT / 2f,
				phoneTexture.getWidth()  / 200.0f,
				phoneTexture.getHeight() / 200.0f);


		soundEffect = Gdx.audio.newSound(Gdx.files.internal("beep.wav"));

		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("soviet-anthem.mp3"));

		backgroundMusic.setLooping(true);
		backgroundMusic.play();

		batch = new SpriteBatch();
		gorbaTexture = new Texture("gorba.png");

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		if(Gdx.input.isTouched()) {
			int realX = Gdx.input.getX();
			int realY = Gdx.input.getY();

			Vector3 touchPos = new Vector3(realX, realY, 0);

			camera.unproject(touchPos);

			gorbaRectangle.x = touchPos.x;
			gorbaRectangle.y = touchPos.y;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			gorbaRectangle.x = gorbaRectangle.x + GORBA_SPEED * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			gorbaRectangle.x = gorbaRectangle.x - GORBA_SPEED * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			gorbaRectangle.y = gorbaRectangle.y + GORBA_SPEED * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			gorbaRectangle.y = gorbaRectangle.y - GORBA_SPEED * Gdx.graphics.getDeltaTime();
		}

		batch.draw(gorbaTexture, gorbaRectangle.x, gorbaRectangle.y, gorbaRectangle.width, gorbaRectangle.height);
		batch.draw(phoneTexture, phoneRectangle.x, phoneRectangle.y, phoneRectangle.width, phoneRectangle.height);

		if(phoneRectangle.overlaps(gorbaRectangle)) {
			soundEffect.play();
			phoneRectangle.x = MathUtils.random(0f, WORLD_WIDTH - phoneRectangle.width);
			phoneRectangle.y = MathUtils.random(0f, WORLD_HEIGHT - phoneRectangle.height);
		}
		batch.end();
	}

	@Override
	public void dispose () {
		gorbaTexture.dispose();
		phoneTexture.dispose();
		soundEffect.dispose();
		backgroundMusic.dispose();
	}
}
