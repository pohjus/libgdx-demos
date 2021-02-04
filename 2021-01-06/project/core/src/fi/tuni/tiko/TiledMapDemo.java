package fi.tuni.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class TiledMapDemo extends ApplicationAdapter {

	boolean upleft, downleft, upright, downright;

	/**
	 * Window width in pixels
	 */
	private final int WINDOW_WIDTH = 800;

	/**
	 * Window height in pixels
	 */
	private final int WINDOW_HEIGHT = 480;


	/*
	 * Map size is 42 x 42 tiles.
	 */
	int TILES_AMOUNT_WIDTH  = 42;
	int TILES_AMOUNT_HEIGHT = 42;

	/**
	 * One tile is 32
	 */
	int TILE_WIDTH          = 32;
	int TILE_HEIGHT         = 32;

	/**
	 * World in pixels
	 * WORLD_HEIGHT_PIXELS = 42 * 32 = 1344 pixels
	 * WORLD_WIDTH_PIXELS  = 42 * 32 = 1344 pixels
	 */
	int WORLD_HEIGHT_PIXELS = TILES_AMOUNT_HEIGHT * TILE_HEIGHT;
	int WORLD_WIDTH_PIXELS  = TILES_AMOUNT_WIDTH  * TILE_WIDTH;

	/**
	 * Player sprite
	 */
	private Sprite playerSprite;

	/**
	 * Player speed
	 */
	private float SPEED = 120f;

	/**
	 * Tile map to be used in the world.
	 *
	 * Map is a set of MapLayers. MapLayer contains set of MapObjects.
	 * MapObjects can be TextureMapObject, RectangleObject, CircleMapObject..
	 *
	 * Some layers maybe special layers (has more functionality) like
	 * TiledMapTileLayer.
	 *
	 * Example accessing a layer:
	 *    TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
	 */
	private TiledMap tiledMap;

	/**
	 * To render the tile map
	 *
	 * MapRenered allows you to render the layers and objects of a map.
	 * Before you start, you must specify the view on your map; what
	 * part of the map is visible. Easiest way to do this is to
	 * tell the map about your camera:
	 *     tiledMapRenderer.setView(camera)
	 *
	 * To render all the layers
	 *     tiledMapRenderer.render();
	 */
	private TiledMapRenderer tiledMapRenderer;

	/**
	 * To render the sprite
	 */
	SpriteBatch sb;

	/**
	 * On 3D in here, we will be using OrthographicCamera
	 */
	private OrthographicCamera camera;

	/**
	 * For debugging purposes
	 */
	String TAG = "MyTiledMap";



	@Override
	public void create () {
		sb = new SpriteBatch();

		// Create the sprite and set it to 32, 32. World has walls in corners (32,32)
		// so instead of putting the sprite in 0, 0 (in walls) we will use 32,32

		playerSprite = new Sprite(new Texture(Gdx.files.internal("fatman.png")));
		playerSprite.setX( TILE_WIDTH );
		playerSprite.setY( TILE_WIDTH );

		// create camera
		camera = new OrthographicCamera();

		// Show always area of our world 800 x 480
		camera.setToOrtho(false,         // y points up
				WINDOW_WIDTH,            // width
				WINDOW_HEIGHT);          // height


		// Loads the map.txt using TmxMapLoader
		tiledMap = new TmxMapLoader().load("map.tmx");

		// Top down map (Orthogonal) (also Isometric available)
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		// move camera to player sprite
		moveCamera();
	}

	@Override
	public void render () {
		clearScreen();

		// Update the camera so movement is visible
		camera.update();
		// Which part of the map are we looking? Use camera's viewport
		tiledMapRenderer.setView(camera);

		// Use coordinate system specified by the camera
		sb.setProjectionMatrix(camera.combined);

		// Time that has elapsed from previous render call
		float delta = Gdx.graphics.getDeltaTime();

		// Check if player has collided with collectable (uses object layer)
		checkCollisions();

		// Move player using arrow keys
		movePlayer(delta);

		// Move camera
		moveCamera();

		// Render all layers
		tiledMapRenderer.render();

		// render sprite
		sb.begin();
		playerSprite.draw(sb);
		sb.end();

	}

	/**
	 * Checks if player has collided with collectable
	 */
	private void checkCollisions() {

		// Let's get the collectable rectangles layer
		MapLayer collisionObjectLayer = (MapLayer)tiledMap.getLayers().get("collectable-rectangles");

		// All the rectangles of the layer
		MapObjects mapObjects = collisionObjectLayer.getObjects();

		// Cast it to RectangleObjects array
		Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

		// Iterate all the rectangles
		for (RectangleMapObject rectangleObject : rectangleObjects) {
			Rectangle rectangle = rectangleObject.getRectangle();
			if (playerSprite.getBoundingRectangle().overlaps(rectangle)) {
				// Get the x and y of the rectangle that is on top
				// of the collectable and clear the tile under it
				clearIt(rectangle.getX(), rectangle.getY());
			}
		}
	}


	/**
	 * Clears collectable from the given coordinates
	 * @param xCoord coordinate X
	 * @param yCoord coordinate Y
	 */
	private void clearIt(float xCoord, float yCoord) {
		int indexX = (int) xCoord / TILE_WIDTH;
		int indexY = (int) yCoord / TILE_HEIGHT;

		TiledMapTileLayer wallCells = (TiledMapTileLayer) tiledMap.getLayers().get("collectable-items-layer");
		wallCells.setCell(indexX, indexY, null);
	}

	private void clearScreen() {
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void movePlayer(float delta) {
		float moveAmount = SPEED * delta;

		// Room in left? modifies the upleft, downleft, upright and upleft attributes!
		getMyCorners(playerSprite.getX() + -1 * moveAmount,
				playerSprite.getY());


		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) &&
				playerSprite.getX() > TILE_WIDTH &&
				upleft && downleft) {

			playerSprite.translateX(-1 * moveAmount);
		}

		// Room in right
		getMyCorners(playerSprite.getX() + moveAmount,
				playerSprite.getY());

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) &&
				playerSprite.getX() < WORLD_WIDTH_PIXELS - 2 * TILE_WIDTH &&
				upright && downright) {

			playerSprite.translateX(moveAmount);

		}

		// Room in Up
		getMyCorners(playerSprite.getX(), playerSprite.getY() + moveAmount );

		if(Gdx.input.isKeyPressed(Input.Keys.UP) &&
				playerSprite.getY() < WORLD_HEIGHT_PIXELS - 2 * TILE_HEIGHT &&
				upright && upleft) {

			playerSprite.translateY( moveAmount );
		}

		// Room in Down
		getMyCorners(playerSprite.getX(),
				playerSprite.getY() + -1 * moveAmount );

		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) &&
				playerSprite.getY() > TILE_HEIGHT &&
				downright && downleft) {
			playerSprite.translateY(-1 * moveAmount);
		}
	}

	private void moveCamera() {

		camera.position.set(playerSprite.getX(),
				playerSprite.getY(),
				0);

		// Move LEFT if possible
		if(camera.position.x < WINDOW_WIDTH / 2){
			camera.position.x = WINDOW_WIDTH / 2;
		}

		// UP
		if(camera.position.y > WORLD_HEIGHT_PIXELS - WINDOW_HEIGHT / 2) {
			camera.position.y = WORLD_HEIGHT_PIXELS - WINDOW_HEIGHT / 2;
		}

		// DOWN
		if(camera.position.y < WINDOW_HEIGHT / 2) {
			camera.position.y = WINDOW_HEIGHT / 2;
		}

		// RIGHT
		if(camera.position.x > WORLD_WIDTH_PIXELS - WINDOW_WIDTH / 2f) {
			camera.position.x = WORLD_WIDTH_PIXELS - WINDOW_WIDTH / 2f;
		}
	}

	/**
	 * Calculates if player can move to directions.
	 * @param pX player x coord
	 * @param pY player y coord
	 */
	public void getMyCorners(float pX, float pY){
		/*
		 * pX and pY are the coordinates of the player.
		 * coordinates are calculated from the bottom
		 * left position of the texture:
		 *
		 *       XXXXXX
		 *       X    X
		 *       X    X
		 *       PXXXXX
		 *   (px, py)
		 */

		float downYPos  = pY;
		float upYPos    = playerSprite.getHeight() + downYPos;

		/*
		 *  upYPos ----> PXXXXX
		 *               X    X
		 *               X    X
		 *  downYPos---> PXXXXX
		 *
		 *  so if downYPos is 32 and sprite height is 32, upYPos is 32 + 32 = 64
		 */

		float leftXPos  = pX;
		float rightXPos = playerSprite.getWidth()  + leftXPos;

		/*
		 *        XXXXXX
		 *        X    X
		 *        X    X
		 *        PXXXXP
		 *        ^    ^
		 *        |    |
		 * leftXPos    rightXPos
		 *
		 */

		/*
		 *    (x,y)
		 *        PXXXXX
		 *        X    X
		 *        X    X
		 *        XXXXXX
		 *
		 * Is the upleft corner inside of wall?
		 */
		upleft    = isFree(leftXPos,  upYPos);

		/*
		 *        XXXXXX
		 *        X    X
		 *        X    X
		 *        PXXXXX
		 *    (x,y)
		 *
		 * Is the downleft corner inside of wall?
		 */
		downleft  = isFree(leftXPos,  downYPos);

		/*
		 *            (x,y)
		 *        XXXXXP
		 *        X    X
		 *        X    X
		 *        XXXXXX
		 *
		 * Is the upright corner inside of wall?
		 */
		upright   = isFree(rightXPos, upYPos);

		/*
		 *        XXXXXX
		 *        X    X
		 *        X    X
		 *        XXXXXP
		 *            (x,y)
		 *
		 * Is the downright corner inside of wall?
		 */
		downright = isFree(rightXPos, downYPos);
	}

	/**
	 * Checks if there are free space under x and y
	 * @param x player x coord
	 * @param y player y coord
	 * @return true if available space, false if not
	 */
	private boolean isFree(float x, float y) {

		// Calculate player coordinates to tile coordinates
		// example, (34,34) => (1,1)
		int indexX = (int) x / TILE_WIDTH;
		int indexY = (int) y / TILE_HEIGHT;

		TiledMapTileLayer wallCells = (TiledMapTileLayer) tiledMap.getLayers().get("wall-layer");

		// Is the coordinate / cell free?
		if(wallCells.getCell(indexX, indexY) != null) {
			// There was a cell, it's not free
			return false;
		} else {
			// There was no cell, it's free
			return true;
		}
	}
}
