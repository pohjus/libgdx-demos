package fi.tuni.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;


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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MyGdxGame extends ApplicationAdapter {

    boolean upleft, downleft, upright, downright;

    /**
     * Window width in meters
     */
    private final float WINDOW_WIDTH = 8f;

    /**
     * Window height in meters
     */
    private final float WINDOW_HEIGHT = 4.8f;


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
     * Player texture and body
     */
    private Texture playerTexture;
    private Body playerBody;
    private final float PLAYER_RADIUS = 0.15f;

    Array<Body> removalBodies = new Array<Body>();

    /**
     * Player speed
     */
    private float SPEED = 1.20f;

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


    private World world;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create () {
        sb = new SpriteBatch();

        world = new World(new Vector2(0, -9.8f), true);

        // Create the sprite and set it to 3.2, 3.2. World has walls in corners (32,32)
        // so instead of putting the sprite in 0, 0 (in walls) we will use 32,32

        createPlayer();



        // create camera
        camera = new OrthographicCamera();

        // Show always area of our world 8.00 x 4.80
        camera.setToOrtho(false,         // y points up
                WINDOW_WIDTH,            // width
                WINDOW_HEIGHT);          // height


        // Loads the map.txt using TmxMapLoader
        tiledMap = new TmxMapLoader().load("map.tmx");

        // Top down map (Orthogonal) (also Isometric available)
        // Let's scale it down for meters 1 / 100f
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 100f);

        transformWallsToBodies("world-wall-rectangles", "wall");
        transformWallsToBodies("ground-rectangles", "wall");
        transformWallsToBodies("collectable-rectangles", "collectable");

        // move camera to player sprite
        moveCamera();

        debugRenderer = new Box2DDebugRenderer();


        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                String userDataA = (String) (contact.getFixtureA().getBody().getUserData());
                String userDataB = (String) (contact.getFixtureB().getBody().getUserData());

                if(userDataA.equals("collectable")) {
                    contact.setEnabled(false);
                    removalBodies.add(contact.getFixtureA().getBody());
                }
                if(userDataB.equals("collectable")) {
                    contact.setEnabled(false);
                    removalBodies.add(contact.getFixtureB().getBody());
                }

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public void createPlayer() {
        playerTexture = new Texture(Gdx.files.internal("fatman.png"));

        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;

        myBodyDef.position.set(TILE_WIDTH / 100f + PLAYER_RADIUS, TILE_HEIGHT / 100f + PLAYER_RADIUS);

        playerBody = world.createBody(myBodyDef);
        playerBody.setUserData("player");
        FixtureDef playerFixtureDef = new FixtureDef();

        playerFixtureDef.density     = 2;
        playerFixtureDef.restitution = 0.5f;
        playerFixtureDef.friction    = 0.5f;

        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(PLAYER_RADIUS);

        playerFixtureDef.shape = circleshape;
        playerBody.createFixture(playerFixtureDef);
    }

    private void checkUserInput() {

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerBody.applyForceToCenter(new Vector2(-2f, 0), true);
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerBody.applyForceToCenter(new Vector2(2f, 0), true);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            playerBody.applyLinearImpulse(new Vector2(0, 0.5f),
                    playerBody.getWorldCenter(), true);
        }
    }

    @Override
    public void render () {
        sb.setProjectionMatrix(camera.combined);

        clearScreen();
        // Update the camera so movement is visible
        // Which part of the map are we looking? Use camera's viewport
        tiledMapRenderer.setView(camera);



        // check input
        checkUserInput();

        // Move camera
        moveCamera();
        camera.update();

        // Render all layers
        tiledMapRenderer.render();

        // render sprite
        sb.begin();


        sb.draw(playerTexture,
                playerBody.getPosition().x - PLAYER_RADIUS,
                playerBody.getPosition().y - PLAYER_RADIUS,
                PLAYER_RADIUS,                 // originX
                PLAYER_RADIUS,                 // originY
                PLAYER_RADIUS * 2,             // width
                PLAYER_RADIUS * 2,             // height
                1.0f,                          // scaleX
                1.0f,                          // scaleY
                playerBody.getTransform().getRotation() * MathUtils.radiansToDegrees,
                0,                             // Start drawing from x = 0
                0,                             // Start drawing from y = 0
                playerTexture.getWidth(),      // End drawing x
                playerTexture.getHeight(),     // End drawing y
                false,                         // flipX
                false);                        // flipY


        sb.end();
        //debugRenderer.render(world, camera.combined);


        doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }


    private void moveCamera() {

        camera.position.set(playerBody.getPosition().x,
                playerBody.getPosition().y,
                0);

    }


    private double accumulator = 0;
    private float TIME_STEP = 1 / 60f;

    private void doPhysicsStep(float deltaTime) {

        float frameTime = deltaTime;

        // If it took ages (over 4 fps, then use 4 fps)
        // Avoid of "spiral of death"
        if(deltaTime > 1 / 4f) {
            frameTime = 1 / 4f;
        }

        accumulator += frameTime;

        while (accumulator >= TIME_STEP) {
            // It's a fixed time step!
            world.step(TIME_STEP, 8, 3);
            accumulator -= TIME_STEP;
        }
    }

    private void transformWallsToBodies(String layer, String userData) {
        // Let's get the collectable rectangles layer
        MapLayer collisionObjectLayer = tiledMap.getLayers().get(layer);

        // All the rectangles of the layer
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // Cast it to RectangleObjects array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate all the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();

            // SCALE given rectangle down if using world dimensions!
            Rectangle rectangle = scaleRect(tmp, 1 / 100f);

            createStaticBody(rectangle, userData);
        }
    }

    public void createStaticBody(Rectangle rect, String userData) {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;

        float x = rect.getX();
        float y = rect.getY();
        float width = rect.getWidth();
        float height = rect.getHeight();

        float centerX = width/2 + x;
        float centerY = height/2 + y;

        myBodyDef.position.set(centerX, centerY);

        Body wall = world.createBody(myBodyDef);

        wall.setUserData(userData);
        // Create shape
        PolygonShape groundBox = new PolygonShape();

        // Real width and height is 2 X this!
        groundBox.setAsBox(width / 2 , height / 2 );

        wall.createFixture(groundBox, 0.0f);
    }

    private Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangle = new Rectangle();
        rectangle.x      = r.x * scale;
        rectangle.y      = r.y * scale;
        rectangle.width  = r.width * scale;
        rectangle.height = r.height * scale;
        return rectangle;
    }


}
