package com.mygdx.breakout;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BreakoutGameClass extends ApplicationAdapter {
	SpriteBatch batch;
	Texture ball;
	Texture pad;
	Texture tile;
	BitmapFont score;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		ball = new Texture("blueball.png");
		pad = new Texture("brownpad.png");
		tile = new Texture("greentile.png");
		score = new BitmapFont();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		score.draw(batch, "Score = 0", 30, 460);

		batch.draw(pad, 300, 10, 65, 10);
		batch.draw(ball, 315, 20, 15, 15);

		for (int y = 420; y > 200; y-= 20) {
			for (int x = 30; x < 570; x += 65) {
				batch.draw(tile, x, y, 60, 15);
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
//		img.dispose();
	}
}
