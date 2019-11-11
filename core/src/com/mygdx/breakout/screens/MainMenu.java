package com.mygdx.breakout.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainMenu implements Screen {

    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private TextButton buttonPlay, buttonExit;
    private BitmapFont white, black;
    private Label heading;

    @Override
    public void show() {
        stage = new Stage();

        // The fonts of the buttons (white and black)
        white = new BitmapFont(Gdx.files.internal("fonts/ocrawhite.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("fonts/ocrablack.fnt"), false);

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

        buttonExit = new TextButton("EXIT", textButtonStyle);
        buttonExit.pad(20); // Add padding to the button

        table.add(buttonExit);
        table.debug();
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
