package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Pixmap;

class Square
{
     //for x and y, top left is origin
     int squareX; //variable holding the x pos of the square
     int squareY; //variable holding the y pos of the square
     int squareColor; //defining the color of the square 0=white, 1=black
     Square(int squareX, int squareY, int squareColor){
          this.squareY=squareY;
          this.squareX=squareX;
     }
     int getX(){
          return squareX;
     }
     int getY(){
          return squareY;
     }
}
public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
        int squareSize=640/8; //setting size of chessboard squares
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
