package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
     Pixmap getPixmapSquare(){
          Pixmap renderSquare = new Pixmap(640/8, 640/8, Pixmap.Format.RGBA8888);
          if (this.color==0){
               renderSquare.setColor(Color.WHITE);
          }
          if (this.color == 1){
               renderSquare.setColor(Color.BLACK);
          }
          renderSquare.fill();
          return renderSquare;
     }
}
public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
        int squareSize=640/8; //setting size of chessboard squares
	ArrayList <Square> squareObjList = new ArrayList();
        ArrayList <Texture> spriteSquareList = new ArrayList();
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
                for(int i=0; i<squareObjList.size(); i++){
                     Square currentSquare = squareObjList.get(i);
                     Pixmap currentSquareImg = currentSquare.getPixmapSquare();
                     currentSquareImg.setColor(Color.WHITE);
                     currentSquareImg.fill();
                     Texture SquareImg = new Texture(currentSquareImg);
                     //Sprite sprite = new Sprite(SquareImg);
                     spriteSquareList.add(SquareImg);
                     
                }
                //Pixmap pixmap = new Pixmap(640/8, 640/8,Pixmap.Format.RGBA8888);
                //pixmap.setColor(Color.BLACK);
                //pixmap.fill();
                //Texture texture = new Texture(pixmap);
                //pixmap.dispose();
                //Sprite sprite = new Sprite(texture);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
               int i=0;
               Square currentSquare = squareObjList.get(i);  
               batch.begin();    
               batch.draw(spriteSquareList.get(i),0,0);
               batch.end();
               i++;
               if (i==squareObjList.size()){
                    i=0;
               }
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
