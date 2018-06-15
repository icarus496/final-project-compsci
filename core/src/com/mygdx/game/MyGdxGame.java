package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
class Square
{
     //for x and y, top left is origin
     int squareX; //variable holding the x pos of the square
     int squareY; //variable holding the y pos of the square
     int color; //defining the color of the square 0=white, 1=black
     Square(int squareX, int squareY, int squareColor){
          this.squareY=squareY;
          this.squareX=squareX;
          this.color=squareColor;
     }
     int getX(){
          return squareX;
     }
     int getY(){
          return squareY;
     }
     void setColor(int new_color){
          this.color=new_color;
     }
     int getColor(){
          return this.color;
     }
}
public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
        int squareSize=640/8; //setting size of chessboard squares
	ArrayList <Square> squareObjList = new ArrayList();
	@Override
	public void create () { 
            SpriteBatch batch = new SpriteBatch();
                for(int n = 1; n == 64; n++){ //creates an 8 by 8 grid of square objects
                     int squareColor;
                     if (n%2==0){
                          squareColor=0;
                     }
                     else{
                          squareColor=1;
                     }
                     int squareX=(n%8)*squareSize;
                     int squareY=(n%8)*squareSize;
                     squareObjList.add(new Square(squareX,squareY,squareColor));
                }
                 
                for (int i = 0; i < squareObjList.size(); i++){
                     Pixmap square=new Pixmap(squareSize, squareSize, Pixmap.Format.RGBA8888); //defining a pixmap that will draw a square to the screen
                     int squareColor=(squareObjList.get(i)).getColor();
                     if (squareColor == 0){
                          square.setColor(Color.WHITE);
                          square.fill();
                     }
                     if (squareColor == 1){
                          square.setColor(Color.BLACK);
                          square.fill();
                     }
                     batch.add();
                     square.dispose();
                }
                
                
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
