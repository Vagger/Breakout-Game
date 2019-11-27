package com.mygdx.breakout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.breakout.controllers.InputController;

import static java.lang.Math.abs;

public class BreakoutGameClass extends Game {
    public static String TITLE = "Breakout";
    public static String VERSION = "1.0.1";

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private int score = 0;
    private String scoreText;
    private String controlsText;
    private BitmapFont font;

    private boolean gameStarted;
    private boolean gameOver;

    private float padSpeed = 500;
    private float ballSpeed = 999;

    private final Vector2 BALL_INITIAL_POSITION = new Vector2(0, -187);
    private final Vector2 PAD_INITIAL_POSITION = new Vector2(0, -195);

    private Body pad;
    private Body ball;
    private Body walls;

    private Sound tick;

    private static final float TIMESTEP = 1 / 60f; // frames per second
    private static final int VELOCITYITERATIONS = 8, POSITIONITERATION = 3; // Common values

    @Override
    public void create() {
        score = 0;
        scoreText = "score: ";
        controlsText = "Press space to start the game";
        font = new BitmapFont();

        // World and its gravity and stuff
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        // Camera
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // 1:1 ratio

        // Sounds
        tick = Gdx.audio.newSound(Gdx.files.internal("sounds/tick.mp3"));

        // Batch
        batch = new SpriteBatch();

        gameStarted = false;

        addPad();
        addBall();
        addWalls();
        addTiles();
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        debugRenderer.dispose();
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        System.out.println(ball.getLinearVelocity());
        System.out.println(ball.getPosition());
        ball.setLinearVelocity(ball.getLinearVelocity().x * 1.1f, ball.getLinearVelocity().y * 1.1f);

        // INPUT PROCESSOR
        Gdx.input.setInputProcessor(new InputController() {
            @Override
            public boolean keyDown(int keyCode) {
                switch (keyCode) {
                    case Input.Keys.ESCAPE:
                        Gdx.app.exit();
                        break;
                    case Input.Keys.SPACE:
                        if (!gameStarted) {
                            ball.setLinearVelocity(ballSpeed, ballSpeed);
                            gameStarted = true;
                            controlsText = "Move the pad with left and right arrows";
                            break;
                        }
                    case Input.Keys.LEFT:
                        pad.setLinearVelocity(-padSpeed, 0);
                        break;
                    case Input.Keys.RIGHT:
                        pad.setLinearVelocity(padSpeed, 0);
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(int keyCode) {
                switch (keyCode) {
                    case Input.Keys.LEFT:
                    case Input.Keys.RIGHT:
                        pad.setLinearVelocity(0, 0);
                        break;
                }
                return true;
            }
        });

        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);

        batch.begin();

        // HOW TO PLAY
        font.setColor(1, 1, 1, 1); //white
        font.draw(batch, controlsText, 220, 30);

        // YOU LOST
//        if (ball.getPosition().y < BALL_INITIAL_POSITION.y) {
//            scoreText = "Game Over";
//            font.setColor(1, 0, 0, 1); //red
//            font.draw(batch, scoreText, 40, 420);
//            font.draw(batch, "You scored " + score, 15, 400);
//            gameOver = true;
//        }
        // YOU WON
        if (world.getBodyCount() == 3) { // Only ball, pad and walls
            scoreText = "You Won!";
            font.setColor(0, 1, 0, 1); //green
            font.draw(batch, scoreText, 40, 420);
            font.draw(batch, "You scored " + score, 15, 400);
            gameOver = true;
        }

        Array<Contact> intersectedBodies;
        intersectedBodies = world.getContactList();
        if (world.getContactCount() > 1) {
            for (Contact contact : intersectedBodies) {
                if (contact.isTouching()) {
                    Body contactedBodyA = contact.getFixtureA().getBody();
                    Body contactedBodyB = contact.getFixtureB().getBody();

                    // Ball hits tile
                    if (!contactedBodyA.equals(ball) && !contactedBodyA.equals(pad) && !contactedBodyA.equals(walls)) {
                        score += 20 + Math.floor(Math.random() * 10);
                        ball.setLinearVelocity(ball.getLinearVelocity().x * 2.5f, ball.getLinearVelocity().y * 2.5f);
                        world.destroyBody(contactedBodyA);
                        tick.play();
                    }
                    if (!contactedBodyB.equals(ball) && !contactedBodyB.equals(pad) && !contactedBodyB.equals(walls)) {
                        score += 20 + Math.floor(Math.random() * 10);
                        ball.setLinearVelocity(ball.getLinearVelocity().x * 2.5f, ball.getLinearVelocity().y * 2.5f);
                        world.destroyBody(contactedBodyB);
                        tick.play();
                    }

                    // Ball hits pad
                    if (contactedBodyA.equals(ball) && contactedBodyB.equals(pad)
                            || contactedBodyA.equals(pad) && contactedBodyB.equals(ball)) {
                        ball.setLinearVelocity(ball.getLinearVelocity().x, abs(ball.getLinearVelocity().y));
                    }

                    // Ball hits floor
                    if (contactedBodyA.equals(ball) && contactedBodyB.equals(walls) && ball.getPosition().y < -193
                            || contactedBodyA.equals(walls) && contactedBodyB.equals(ball) && ball.getPosition().y < -193) {
                        scoreText = "Game Over";
                        font.setColor(1, 0, 0, 1); //red
                        font.draw(batch, scoreText, 40, 420);
                        font.draw(batch, "You scored " + score, 15, 400);
                        gameOver = true;
                    }
                }
            }

        }

        if (!gameOver) {
            font.setColor(1, 1, 1, 1); //white
            font.draw(batch, scoreText + score, 40, 420);
        }

        batch.end();

        if (gameOver) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameStarted = false;
            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);
            for (Body body : bodies) {
                world.destroyBody(body);
            }

            // Make everything initial
            ball.getPosition().set(BALL_INITIAL_POSITION);
            ball.setLinearVelocity(0, 0);
            pad.getPosition().set(PAD_INITIAL_POSITION);
            pad.setLinearVelocity(0, 0);

            this.create();
        }

        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    private void addBall() {
        // Ball definition
        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(BALL_INITIAL_POSITION);

        // Ball shape
        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(5f);

        // Ball Fixture definition
        FixtureDef ballFixtureDef = new FixtureDef();
        ballFixtureDef.shape = ballShape;
        ballFixtureDef.density = 2; // 2.5 kg in 1 m^2
        ballFixtureDef.friction = 0; // 0 = slides like a mofo, 1 = doesn't slide
        ballFixtureDef.restitution = 1; // not losing velocity when falling down

        ball = world.createBody(ballDef);
        ball.createFixture(ballFixtureDef); // Ball

        ballShape.dispose();
    }

    private void addPad() {
        // Pad definition
        BodyDef padDef = new BodyDef();
        padDef.type = BodyDef.BodyType.DynamicBody;
        padDef.position.set(PAD_INITIAL_POSITION);

        // Pad shape
        PolygonShape padShape = new PolygonShape();
        padShape.setAsBox(55, 7);

        // Pad fixture def
        FixtureDef padFixtureDef = new FixtureDef();
        padFixtureDef.shape = padShape;
        padFixtureDef.friction = 0;
        padFixtureDef.restitution = 0;

        pad = world.createBody(padDef);
        pad.createFixture(padFixtureDef); // Pad

        padShape.dispose();
    }

    private void addWalls() {
        // Walls definition
        BodyDef wallsDef = new BodyDef();
        wallsDef.type = BodyDef.BodyType.StaticBody;
        wallsDef.position.set(0, 0);

        // Walls shape
        ChainShape wallsShape = new ChainShape();
        wallsShape.createChain(new Vector2[]{new Vector2(-200, -200),
                new Vector2(-200, 200),
                new Vector2(200, 200),
                new Vector2(200, -200),
                new Vector2(-200, -200)});

        // Walls fixture def
        FixtureDef wallsFixtureDef = new FixtureDef();
        wallsFixtureDef.shape = wallsShape;
        wallsFixtureDef.friction = 1;

        walls = world.createBody(wallsDef);
        walls.createFixture(wallsFixtureDef); // Walls

        wallsShape.dispose();
    }

    private void addTiles() {
        for (int ypos = 150; ypos > 30; ypos -= 30) {
            for (int xpos = -150; xpos < 170; xpos += 50) {
                BodyDef tileDef = new BodyDef();
                tileDef.type = BodyDef.BodyType.StaticBody;
                tileDef.position.set(xpos, ypos);

                // Tile shape
                PolygonShape tileShape = new PolygonShape();
                tileShape.setAsBox(20, 10);

                // Tile fixture def
                FixtureDef tileFixtureDef = new FixtureDef();
                tileFixtureDef.shape = tileShape;

                // Tile body
                Body tile = world.createBody(tileDef);
                tile.createFixture(tileFixtureDef);

            }
        }
    }
}
