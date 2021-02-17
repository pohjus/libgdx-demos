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

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;

    public static final boolean DEBUG_PHYSICS = true;
    public static final float WORLD_WIDTH     = 8.0f;
    public static final float WORLD_HEIGHT    = 4.8f;
    public static Texture tennisBallTexture;
    public static Texture backgroundTexture;

    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    private World world;
    private Body tennisBallBody;

    // All bodies
    Array<Body> bodies = new Array<Body>();

    public enum GameObjectType {
        TENNISBALL,
        BULLET
    }

    class GameObjectInfo {
        public Texture texture;
        public float radius;
        public int id;

        GameObjectType type;

        public GameObjectInfo(Texture t, float r, GameObjectType got) {
            texture = t;
            radius = r;
            type = got;
        }
    }

    GameObjectInfo tennisBallGameObject;

    @Override
    public void create() {
        tennisBallTexture = new Texture("tennis.png");
        backgroundTexture = new Texture("background.png");

        tennisBallGameObject = new GameObjectInfo(tennisBallTexture, 0.5f, GameObjectType.TENNISBALL);

        // Create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        // Create debug renderer.
        debugRenderer = new Box2DDebugRenderer();

        // Create world and set it's gravity!
        // true => allow body sleeping. Bodies
        // are excluded from the simulation until something happens
        // to 'wake' them again.
        world = new World(new Vector2(0, -9.8f), true);

        // Create one body to the world
        tennisBallBody = createBody(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, tennisBallGameObject.radius);
        tennisBallBody.setUserData(tennisBallGameObject);


        // Create ground to the world
        createGround();

        // Create SpriteBatch
        batch = new SpriteBatch();

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                // Game objects collide with each other

                // Let's get user data from both of the objects
                // We do not know the order:
                Object userData1 = contact.getFixtureA().getBody().getUserData();
                Object userData2 = contact.getFixtureB().getBody().getUserData();

                // If we did get user data (ground does not have user data)
                if(userData1 != null && userData2 != null) {
                    GameObjectInfo data1 = (GameObjectInfo) userData1;
                    GameObjectInfo data2 = (GameObjectInfo) userData2;

                    // The order is unknown so we check this either way
                    // If bullet hit tennisball
                    if((data1.type == GameObjectType.BULLET &&
                            data2.type == GameObjectType.TENNISBALL) ||
                            (data1.type == GameObjectType.TENNISBALL &&
                                    data2.type == GameObjectType.BULLET)) {
                        Gdx.app.log("CRASH", "Tennisball hit bullet");
                    }

                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    private Body createBody(float x, float y, float radius) {
        Body playerBody = world.createBody(getDefinitionOfBody(x, y));
        playerBody.createFixture(getFixtureDefinition(radius));
        return playerBody;
    }

    private BodyDef getDefinitionOfBody(float x, float y) {
        // Body Definition
        BodyDef myBodyDef = new BodyDef();

        // It's a body that moves
        myBodyDef.type = BodyDef.BodyType.DynamicBody;

        // Initial position is centered up
        // This position is the CENTER of the shape!
        myBodyDef.position.set(x, y);

        return myBodyDef;
    }

    private FixtureDef getFixtureDefinition(float radius) {
        FixtureDef playerFixtureDef = new FixtureDef();

        // Mass per square meter (kg^m2)
        playerFixtureDef.density = 1;

        // How bouncy object? Very bouncy [0,1]
        playerFixtureDef.restitution = 0.5f;

        // How slipper object? [0,1]
        playerFixtureDef.friction = 0.5f;

        // Create circle shape.
        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(radius);

        // Add the shape to the fixture
        playerFixtureDef.shape = circleshape;

        return playerFixtureDef;
    }

    private void clearScreen(float r, float g, float b) {
        Gdx.gl.glClearColor(r, g, b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);

        clearScreen(0, 0, 0);



        // populate the array with bodies
        world.getBodies(bodies);

        batch.begin();

        // Draw background
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        checkUserInput();

        drawAllBodies();

        batch.end();

        if(DEBUG_PHYSICS) {
            debugRenderer.render(world, camera.combined);
        }

        clearBodies();

        doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    public void clearBodies() {

        // If tennisball off screen
        if(tennisBallBody.getPosition().y < -1 * tennisBallGameObject.radius*2) {
            // Clear velocity (dropping of the screen)
            tennisBallBody.setLinearVelocity(new Vector2(0,0));
            // Move the body
            tennisBallBody.setTransform(new Vector2(WORLD_WIDTH / 2, WORLD_HEIGHT + tennisBallGameObject.radius*2), 0);
        }

        Array<Body> bodiesToBeDestroyed = new Array<Body>();

        // Iterate all bullets
        for (Body body : bodies) {
            // If it's not ground
            if(body.getUserData() != null) {
                GameObjectInfo info = (GameObjectInfo) body.getUserData();
                // If it's bullet, then mark it to be removed.
                if (info.type == GameObjectType.BULLET) {
                    float yPos = body.getPosition().y;
                    if (yPos < -1 * info.radius * 2) {
                        bodiesToBeDestroyed.add(body);
                    }

                }
            }
        }

        // Destroy needed bodies
        for (Body body : bodiesToBeDestroyed) {
            world.destroyBody(body);
        }

    }
    private void checkUserInput() {
        // KEY INPUT FOR DESKTOP
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            tennisBallBody.applyForceToCenter(new Vector2(-5f, 0), true);
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            tennisBallBody.applyForceToCenter(new Vector2(5f, 0), true);
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            tennisBallBody.applyLinearImpulse(new Vector2(0, 0.5f), tennisBallBody.getWorldCenter(), true);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fireBullet();
        }
    }

    private void fireBullet() {
        GameObjectInfo fireBulletInfo = new GameObjectInfo(tennisBallTexture, 0.1f, GameObjectType.BULLET);

        float padding = 0.01f;

        // Create body is my own method
        Body bulletBody = createBody(tennisBallBody.getPosition().x,
                tennisBallBody.getPosition().y +
                        tennisBallGameObject.radius +
                        fireBulletInfo.radius + padding, fireBulletInfo.radius);

        bulletBody.setBullet(true);

        bulletBody.setUserData(fireBulletInfo);

        float xForce = MathUtils.random(-0.2f, +0.2f);
        bulletBody.applyLinearImpulse(new Vector2(xForce, 0.2f),
                bulletBody.getWorldCenter(),
                true);
    }

    private void drawAllBodies() {

        Gdx.app.log("", "Number of bodies: " + bodies.size);

        // Draw all bodies
        for (Body body : bodies) {

            // Draw all bodies with user data (ground is not drawn)
            if(body.getUserData() != null) {

                // Get user data, has texture, type (bullet, or tennisball) and
                // radius
                GameObjectInfo info = (GameObjectInfo) body.getUserData();

                batch.draw(info.texture,
                        body.getPosition().x - info.radius,
                        body.getPosition().y - info.radius,
                        info.radius,                   // originX
                        info.radius,                   // originY
                        info.radius * 2,               // width
                        info.radius * 2,               // height
                        1.0f,                          // scaleX
                        1.0f,                          // scaleY
                        body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                        0,                             // Start drawing from x = 0
                        0,                             // Start drawing from y = 0
                        info.texture.getWidth(),       // End drawing x
                        info.texture.getHeight(),      // End drawing y
                        false,                         // flipX
                        false);                        // flipY

            }
        }

    }


    private BodyDef getGroundBodyDef() {
        // Body Definition
        BodyDef myBodyDef = new BodyDef();

        // This body won't move
        myBodyDef.type = BodyDef.BodyType.StaticBody;

        // Initial position is centered up
        // This position is the CENTER of the shape!
        myBodyDef.position.set(WORLD_WIDTH / 2, 0.25f);

        return myBodyDef;
    }

    private PolygonShape getGroundShape() {
        // Create shape
        PolygonShape groundBox = new PolygonShape();

        // Real width and height is 2 X this!
        groundBox.setAsBox( WORLD_WIDTH/2 , 0.25f);

        return groundBox;
    }

    public void createGround() {
        Body groundBody = world.createBody(getGroundBodyDef());

        // Add shape to fixture, 0.0f is density.
        // Using method createFixture(Shape, density) no need
        // to create FixtureDef object as on createPlayer!
        groundBody.createFixture(getGroundShape(), 0.0f);
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
            // It's fixed time step!
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
    }

    public void dispose() {
        tennisBallTexture.dispose();
        world.dispose();
    }
}
