package com.mygdx.breakout.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.breakout.controllers.InputController;

import java.util.ArrayList;
import java.util.List;

public class Gameplay implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    private boolean gameStarted = false;
    private float padSpeed = 500;
    private float ballSpeed = 5000;
    private Vector2 padMovement = new Vector2(0,0);
    private Vector2 ballMovement = new Vector2(0,0);

    private Body pad;
    private Body ball;
    private Body walls;

    private static final float TIMESTEP = 1 / 60f; // frames per second
    private static final int VELOCITYITERATIONS = 8, POSITIONITERATION = 3; // Common values

    @Override
    public void show() {
        // World and its gravity and stuff
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        // Camera
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // 1:1 ratio

        // INPUT PROCESSOR
        Gdx.input.setInputProcessor(new InputController() {
            @Override
            public boolean keyDown(int keyCode) {
                switch (keyCode) {
                    case Input.Keys.ESCAPE:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                        break;
                    case Input.Keys.SPACE:
                        ball.setLinearVelocity(ballSpeed, ballSpeed);
//                        ballMovement.x = ballSpeed;
//                        ballMovement.y = ballSpeed;
                    case Input.Keys.LEFT:
                        pad.setLinearVelocity(-padSpeed, 0);
//                        padMovement.x = -padSpeed;
                        break;
                    case Input.Keys.RIGHT:
                        pad.setLinearVelocity(padSpeed, 0);
//                        padMovement.x = padSpeed;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(int keyCode) {
                switch (keyCode) {
                    case Input.Keys.LEFT:
                    case Input.Keys.RIGHT:
                        pad.setLinearVelocity(0,0);
//                        padMovement.x = 0;
                        break;
                }
                return true;
            }
        });

        // Pad definition
        final BodyDef padDef = new BodyDef();
        padDef.type = BodyDef.BodyType.DynamicBody;
        padDef.position.set(0, 0);

        // Pad shape
        PolygonShape padShape = new PolygonShape();
        padShape.set(new Vector2[]{new Vector2(-40, -200),
                new Vector2(-40, -195),
                new Vector2(40, -195),
                new Vector2(40, -200),
                new Vector2(-40, -200)});

        // Pad fixture def
        FixtureDef padFixtureDef = new FixtureDef();
        padFixtureDef.shape = padShape;
        padFixtureDef.friction = 1;
        padFixtureDef.restitution = 0;

        // Ball definition
        final BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(0, -190);

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
        final BodyDef tileDef = new BodyDef();
        tileDef.type = BodyDef.BodyType.StaticBody;

        for (int ypos = 190; ypos > 30; ypos -= 30) {
            for (int xpos = -170; xpos < 150; xpos += 50) {
                // Tile shape
                PolygonShape tileShape = new PolygonShape();
                tileShape.set(new Vector2[]{new Vector2(xpos, ypos),
                        new Vector2(xpos+40, ypos),
                        new Vector2(xpos+40, ypos-20),
                        new Vector2(xpos, ypos-20),
                        new Vector2(xpos, ypos)});

                // Tile fixture def
                FixtureDef tileFixtureDef = new FixtureDef();
                tileFixtureDef.shape = tileShape;

                // Tile body
                world.createBody(tileDef).createFixture(tileFixtureDef);
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
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);

//        pad.setLinearVelocity(10, 0);
//        ball.setLinearVelocity(10,0);

//        pad.applyForceToCenter(padMovement, true);
//        ball.applyForceToCenter(ballMovement, true);

        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }

}
