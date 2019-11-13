package com.mygdx.breakout.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.breakout.controllers.InputController;
import com.sun.net.httpserver.Filter;

public class Gameplay implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    private static final float TIMESTEP = 1 / 60f; // frames per second
    private static final int VELOCITYITERATIONS = 8, POSITIONITERATION = 3; // Common values

    @Override
    public void show() {
        // World and its gravity and stuff
        world = new World(new Vector2(0,0), true);
        debugRenderer = new Box2DDebugRenderer();

        // Camera
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // 1:1 ratio

        // Press Esc, return to Main Menu
        Gdx.input.setInputProcessor(new InputController() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.ESCAPE) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                }
                return true;
            }
        });

        // Pad definition
        final BodyDef padDef = new BodyDef();
        padDef.type = BodyDef.BodyType.DynamicBody;
        padDef.position.set(0, 0);

        Gdx.input.setInputProcessor(new InputController() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.RIGHT) {
                    padDef.linearVelocity.set(20, 0);
                }
                if (keyCode == Input.Keys.LEFT) {
                    padDef.linearVelocity.set(-20, 0);
                }
                return true;
            }
        });

        // Pad shape
        ChainShape padShape = new ChainShape();
        padShape.createChain(new Vector2[]{new Vector2(-50, -200),
                new Vector2(-50, -195),
                new Vector2(50, -195),
                new Vector2(50, -200),
                new Vector2(-50, -200)});

        // Ground fixture def
        FixtureDef padFixtureDef = new FixtureDef();
        padFixtureDef.shape = padShape;
        padFixtureDef.friction = 1;
        padFixtureDef.restitution = 0;

        // Ball definition
        final BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(0, -190);

        ballDef.angularVelocity = 0;

        // Ball shape
        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(5f);

        // Ball Fixture definition
        FixtureDef ballFixtureDef = new FixtureDef();
        ballFixtureDef.shape = ballShape;
        ballFixtureDef.density = 1; // 2.5 kg in 1 m^2
        ballFixtureDef.friction = 0; // 0 = slides like a mofo, 1 = doesn't slide
        ballFixtureDef.restitution = 1; // not losing velocity when falling down

        // Ground definition
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(0, 0);

        // Ground shape
        ChainShape groundShape = new ChainShape();
        groundShape.createChain(new Vector2[]{new Vector2(-200, -200),
                                                new Vector2(-200, 200),
                                                new Vector2(200, 200),
                                                new Vector2(200, -200),
                                                new Vector2(-200, -200)});

        // Ground fixture def
        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;
        groundFixtureDef.friction = 1;
        groundFixtureDef.restitution = 0;

        // Create bodies
        world.createBody(padDef).createFixture(padFixtureDef); // Pad
        world.createBody(ballDef).createFixture(ballFixtureDef); // Ball
        world.createBody(groundDef).createFixture(groundFixtureDef); // Ground

        ballShape.dispose();
        groundShape.dispose();
        padShape.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(world, camera.combined);

        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);
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
