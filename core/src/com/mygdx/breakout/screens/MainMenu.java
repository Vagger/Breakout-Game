package com.mygdx.breakout.screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.breakout.tween.ActorAccessor;
import sun.java2d.pipe.AAShapePipe;

public class MainMenu implements Screen {

    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private TextButton buttonPlay, buttonExit;
    private BitmapFont white, black;
    private Label heading;

    private TweenManager tweenManager;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        // The fonts of the buttons (white and black)
        white = new BitmapFont(Gdx.files.internal("fonts/sylfaenwhite.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("fonts/sylfaenblack.fnt"), false);

        atlas = new TextureAtlas("ui/button.pack");
        skin = new Skin(atlas);

        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.up");
        textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1; // When pressed, the text on the button moves right by 1
        textButtonStyle.pressedOffsetY = -1; // When pressed, the text on the button moves down by 1
        textButtonStyle.font = black;

        // Buttons
        // Exit button
        buttonExit = new TextButton("EXIT", textButtonStyle);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        buttonExit.pad(15); // Add padding to the button

        // Play button
        buttonPlay = new TextButton("PLAY", textButtonStyle);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Levels()); // Go to levels screen
            }
        });
        buttonPlay.pad(15);

        // Heading
        heading = new Label("Breakout", new Label.LabelStyle(white, Color.WHITE));
        heading.setFontScale(2);

        // Table
        table.add(heading);
        table.getCell(heading).spaceBottom(15);
        table.row();
        table.add(buttonPlay);
        table.getCell(buttonPlay).spaceBottom(15);
        table.row();
        table.add(buttonExit);
        table.debug(); // TODO: remove later
        stage.addActor(table);

        // Tween Manager for animations
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        // Sequential animation for heading and buttons
        // Heading colors
        Timeline.createSequence().beginSequence()
                .push(Tween.to(heading, ActorAccessor.RGB, 1f).target(0,0,1))
                .push(Tween.to(heading, ActorAccessor.RGB, 1f).target(0,1,0))
                .push(Tween.to(heading, ActorAccessor.RGB, 1f).target(1,0,0))
                .push(Tween.to(heading, ActorAccessor.RGB, 1f).target(1,1,0))
                .push(Tween.to(heading, ActorAccessor.RGB, 1f).target(0,1,1))
                .push(Tween.to(heading, ActorAccessor.RGB, 1f).target(1,0,1))
                .push(Tween.to(heading, ActorAccessor.RGB, 1f).target(1,1,1))
                .end().repeat(Tween.INFINITY, 0).start(tweenManager);

        // Fade in buttons
        Timeline.createSequence().beginSequence()
                .push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
                .push(Tween.set(buttonExit, ActorAccessor.ALPHA).target(0))
                .push(Tween.from(heading, ActorAccessor.ALPHA, 0.5f).target(0))
                .push(Tween.to(buttonPlay, ActorAccessor.ALPHA, 0.5f).target(1))
                .push(Tween.to(buttonExit, ActorAccessor.ALPHA, 0.5f).target(1))
                .end().start(tweenManager);
    }

    @Override
    public void resize(int width, int height) {
        // Enables table resizing
        table.setClip(true);
        // Sets the table size according to the screen
        table.setSize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        skin.dispose();
        white.dispose();
        black.dispose();
    }
}
