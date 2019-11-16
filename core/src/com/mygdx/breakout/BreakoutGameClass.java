package com.mygdx.breakout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.breakout.screens.Gameplay;
import com.mygdx.breakout.screens.Splash;

public class BreakoutGameClass extends Game {
    public static String TITLE = "Breakout";
    public static String VERSION = "0.0.1";

    SpriteBatch batch;
    Texture ball;
    Texture pad;
    Texture tile;
    BitmapFont score;

    @Override
    public void create() {
//        setScreen(new Splash());
        setScreen(new Gameplay());
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
