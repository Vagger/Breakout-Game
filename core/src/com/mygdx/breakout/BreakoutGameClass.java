package com.mygdx.breakout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.breakout.screens.Splash;

public class BreakoutGameClass extends Game {
    public static String TITLE = "Breakout VERSION 0.0.1";

    SpriteBatch batch;
    Texture ball;
    Texture pad;
    Texture tile;
    BitmapFont score;

    @Override
    public void create() {
        // LOG
        Gdx.app.log(TITLE, "create()");

        setScreen(new Splash());

//        batch = new SpriteBatch();
//        ball = new Texture("blueball.png");
//        pad = new Texture("brownpad.png");
//        tile = new Texture("greentile.png");
//        score = new BitmapFont();
    }

    @Override
    public void dispose() {
        // LOG
        Gdx.app.log(TITLE, "dispose()");
        super.dispose();

//        batch.dispose();
//        ball.dispose();
//        pad.dispose();
//        tile.dispose();
//        score.dispose();
    }

    @Override
    public void render() {
        // LOG
        Gdx.app.log(TITLE, "render()");
        super.render();

//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.begin();
//
//        score.draw(batch, "Score = 0", 30, 460);
//
//        batch.draw(pad, 300, 10, 65, 10);
//        batch.draw(ball, 315, 20, 15, 15);
//
//        for (int y = 420; y > 200; y -= 20) {
//            for (int x = 30; x < 570; x += 65) {
//                batch.draw(tile, x, y, 60, 15);
//            }
//        }
//        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // LOG
        Gdx.app.log(TITLE, "resize(..)");
        super.resize(width, height);
    }

    @Override
    public void pause() {
        // LOG
        Gdx.app.log(TITLE, "pause()");
        super.pause();
    }

    @Override
    public void resume() {
        // LOG
        Gdx.app.log(TITLE, "resume()");
        super.resume();
    }
}
