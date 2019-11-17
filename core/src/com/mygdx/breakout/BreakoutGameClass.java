package com.mygdx.breakout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.mygdx.breakout.controllers.InputController;
import com.mygdx.breakout.enums.Level;
import com.mygdx.breakout.screens.MainMenu;

import java.util.ArrayList;
import java.util.List;

public class BreakoutGameClass extends Game {
    public static String TITLE = "Breakout";
    public static String VERSION = "0.0.1";

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    SpriteBatch batch;
    private int score = 0;
    private String scoreText;
    BitmapFont font;

    private Array<Body> bodies = new Array<>();

    private boolean gamePaused;
    private boolean notPlaying;

    private float padSpeed = 500;
    private float ballSpeed = 999;
    private float BALL_INITIAL_MASS = 200;

    private final Vector2 BALL_INITIAL_POSITION = new Vector2(0, -187);
    private final Vector2 PAD_INITIAL_POSITION = new Vector2(0, -195);

    private Body pad;
    private Body ball;
    private List<Body> tiles = new ArrayList<>();
    private Body walls;

    private Level scoreStatus;
    private String levelString;

    Stage stage;

    private static final float TIMESTEP = 1 / 60f; // frames per second
    private static final int VELOCITYITERATIONS = 8, POSITIONITERATION = 3; // Common values

    @Override
    public void create() {
//        setScreen(new Splash());
//        setScreen(new Gameplay());

        score = 0;
        scoreText = "score: ";
        font = new BitmapFont();

        // World and its gravity and stuff
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        // Camera
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // 1:1 ratio

        // Batch
        batch = new SpriteBatch();

        notPlaying = true;
        gamePaused = false;

        // INPUT PROCESSOR
        Gdx.input.setInputProcessor(new InputController() {
            @Override
            public boolean keyDown(int keyCode) {
                switch (keyCode) {
                    case Input.Keys.ESCAPE:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                        break;
                    case Input.Keys.SPACE:
                        if (notPlaying) {
                            ball.setLinearVelocity(ballSpeed, ballSpeed);
                            scoreStatus = Level.REALLY_SLOW_TURTLE;
                            notPlaying = false;
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

        // Tile definition
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

                tiles.add(tile);
            }
        }

        // Create bodies
        pad = world.createBody(padDef);
        pad.createFixture(padFixtureDef); // Pad

        ball = world.createBody(ballDef);
        ball.createFixture(ballFixtureDef); // Ball

        walls = world.createBody(wallsDef);
        walls.createFixture(wallsFixtureDef); // Walls

        ballShape.dispose();
        wallsShape.dispose();
        padShape.dispose();

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

        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);


        if (notPlaying){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.create();
        }

        batch.begin();

        // GAME OVER
        if (ball.getPosition().y < BALL_INITIAL_POSITION.y) {
            scoreText = "Game Over";
            font.setColor(1,0,0,1); //red
            font.draw(batch, scoreText, 40, 420);
            font.draw(batch, "You scored " + score, 15, 400);
            batch.end();
            notPlaying = true;
//            this.create(); // Then restart
        }
        // YOU WON
        else if (world.getBodyCount() == 3) { // Only ball, pad and walls
            scoreText = "You Won!";
            font.setColor(0, 1, 0, 1); //green
            font.draw(batch, scoreText, 40, 420);
            font.draw(batch, "You scored " + score, 15, 400);
            batch.end();
            notPlaying = true;
//            this.create(); // Then restart
        }
        // The game continues
        else {
            font.setColor(1, 1, 1, 1); //white
            font.draw(batch, scoreText + score, 40, 420);
            batch.end();
        }

        Array<Contact> intersectedBodies;
        if (world.getContactCount() > 1) {
            intersectedBodies = world.getContactList();
            for (Contact contact : intersectedBodies) {
                Body contactedBodyA = contact.getFixtureA().getBody();
                if (!contactedBodyA.equals(ball) && !contactedBodyA.equals(pad) && !contactedBodyA.equals(walls)){
                    world.destroyBody(contactedBodyA);
                    score += 20 + Math.floor(Math.random() * 10);
                    System.out.println(ball.getLinearVelocity());
                    ball.setLinearVelocity(ball.getLinearVelocity().x * 1.5f, ball.getLinearVelocity().y * 1.5f);
                }
                Body contactedBodyB = contact.getFixtureB().getBody();
                if (!contactedBodyB.equals(ball) && !contactedBodyB.equals(pad) && !contactedBodyB.equals(walls)){
                    world.destroyBody(contactedBodyB);
                    score += 20 + Math.floor(Math.random() * 10);
                    System.out.println(ball.getLinearVelocity());
                    ball.setLinearVelocity(ball.getLinearVelocity().x * 1.5f, ball.getLinearVelocity().y * 1.5f);

                }
            }
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
}
