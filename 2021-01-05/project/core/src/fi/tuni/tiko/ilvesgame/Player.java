package fi.tuni.tiko.ilvesgame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Player - game object.
 *
 * @author Jussi Pohjolainen
 */
public class Player {

    private Sound ilvesSound;

    /**
     * All the walk images from one .png
     */
    private Texture playerSheetTexture;

    /**
     * Animation for the walking.
     */
    private Animation<TextureRegion> walkAnimation;

    /**
     * Helper variable for counting what frame is current.
     */
    private float stateTime;

    /**
     * The current frame texture from the sheet.
     */
    private TextureRegion currentFrameTexture;

    /**
     * Walking speed.
     */
    private float speedX = 2.0f;

    /**
     * Helper variable for the direction.
     */
    public static boolean RIGHT = true;

    /**
     * Helper variable for the direction.
     */
    public static boolean LEFT = false;

    /**
     * The direction the player is facing.
     */
    private boolean direction = RIGHT;

    /**
     * The x - coord of the player.
     */
    private float x = 0;

    /**
     * The y - coord of the player.
     */
    private float y = 0;

    /**
     * Initial velocity when jumping.
     */
    private float INITIAL_VELOCITY = 0.8f;

    /**
     * The velocity when jumping.
     */
    private float currentVelocityY = INITIAL_VELOCITY;

    /**
     * Earth gravity pulling down when jumping.
     */
    private float gravity = 0.1f;

    /**
     * Is the player in air, jumping?
     */
    private boolean isInAir = false;

    /**
     * Player height in meters
     */
    private float height;

    /**
     * Player width in meters
     */
    private float width;

    public Player() {
        // Load the image.
        playerSheetTexture = new Texture("capguy-walk.png");
        ilvesSound = Gdx.audio.newSound(Gdx.files.internal("ilves.mp3"));

        createWalkAnimation();

        // pixels -> meters
        height = currentFrameTexture.getRegionHeight() / 100f;
        width = currentFrameTexture.getRegionWidth() / 100f;


    }

    /**
     * Creates the walk animation.
     */
    private void createWalkAnimation() {
        final int FRAME_COLS = 8;
        final int FRAME_ROWS = 1;

        /** CREATE THE WALK ANIM **/

        // Calculate the tile width from the sheet
        int tileWidth = playerSheetTexture.getWidth() / FRAME_COLS;

        // Calculate the tile height from the sheet
        int tileHeight = playerSheetTexture.getHeight() / FRAME_ROWS;

        // Create 2D array from the texture (REGIONS of a TEXTURE).
        TextureRegion[][] tmp = TextureRegion.split(playerSheetTexture, tileWidth, tileHeight);

        // Transform the 2D array to 1D
        TextureRegion[] allFrames = Util.toTextureArray( tmp, FRAME_COLS, FRAME_ROWS );

        walkAnimation = new Animation(4 / 60f, allFrames);

        currentFrameTexture = walkAnimation.getKeyFrame(stateTime, true);
    }

    /**
     * Changes the player's direction.
     *
     * @param dir true => right direction, false => left direction.
     */
    public void changeDirection(boolean dir) {
        if(dir != direction) {
            direction = dir;

            // Reverse all textureregions in the sheet.
            Util.flip(walkAnimation);
        }
    }

    /**
     * Update the frame.
     */
    public void walk() {
        // stateTime was initialized to 0.0f
        stateTime += Gdx.graphics.getDeltaTime();

        if(direction == RIGHT) {
            x += speedX * Gdx.graphics.getDeltaTime();
        } else {
            x -= speedX * Gdx.graphics.getDeltaTime();
        }

        // Check boundaries
        if (x <= 0) {
            x = 0;
        }

        if (x >= IlvesGame.WORLD_WIDTH - currentFrameTexture.getRegionWidth() / 100f) {
            x = IlvesGame.WORLD_WIDTH - currentFrameTexture.getRegionWidth() / 100f;
        }

        // stateTime is used to calculate the next frame
        // frameDuration!
        currentFrameTexture = walkAnimation.getKeyFrame(stateTime, true);
    }

    /**
     * Draw the player.
     *
     * @param batch SpriteBatch for drawing textures.
     */
    public void draw(SpriteBatch batch) {
        if(isInAir) {
            liftMe();
        }

        batch.draw(currentFrameTexture, x, y, width, height);
    }

    /**
     * Moves the player character on y-axis imitating jump.
     *
     * Moves the player character on y-axis the amount of currentVelocityY.
     * On every call reduces the amount of velocity to be added by the pre-
     * determined gravity to make the jump be faster in the start and gradually
     * slow down until currentVelocity reaches negative value and character
     * starts to move down on y-axis on every call imitating falling.
     */
    private void liftMe() {
        if (y + currentVelocityY > 0) {
            y += currentVelocityY;
            currentVelocityY = currentVelocityY - gravity;
        } else {
            Gdx.app.log("", "on ground");

            isInAir = false;
            y = 0;
            currentVelocityY = INITIAL_VELOCITY;
        }
    }


    public void jump() {
        isInAir = true;
        ilvesSound.play();
    }
    /**
     * Dispose the texture.
     */
    public void dispose() {
        playerSheetTexture.dispose();
        ilvesSound.dispose();
    }
}

