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
import java.lang.Math;

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
        int squareSize=640/8; //setting size of chessboard squares
        ArrayList <Square> squareObjList = new ArrayList();
        ArrayList <Sprite> spriteSquareList = new ArrayList();
        Sprite cat;
        int squareX=0;
        int rowNum=0;
	@Override
	public void create () { 
                batch = new SpriteBatch();
                for(int n = 1; n <= 64; n++){ //creates an 8 by 8 grid of square objects
                     int squareColor;
                     if (rowNum%2==0)
                     {
                        if (n%2==0){
                             squareColor=0;
                        }
                        else{
                             squareColor=1;
                        }
                     }
                     else
                     {
                        if (n%2==0){
                            squareColor=1;
                        }
                        else{
                            squareColor=0;
                        }   
                     }
                     if (squareX>=640){
                         squareX=0;
                     }
                     int squareY=rowNum*squareSize;
                     //int squareY =((int)(n/8)-1)*squareSize;
                     System.out.println(n+":"+"("+squareX + "," + squareY + ")");
                     squareObjList.add(new Square(squareX,squareY,squareColor));
                     squareX =squareX+squareSize;
                     if (n%8==0){
                         rowNum++;
                     }
                }
                for(int i=0; i<squareObjList.size(); i++){
                     Square currentSquare = squareObjList.get(i);
                     Pixmap currentSquareImg = currentSquare.getPixmapSquare();
                     Texture SquareImg = new Texture(currentSquareImg);
                     Sprite squareSprite = new Sprite(SquareImg);
                     spriteSquareList.add(squareSprite);
                     
                } 
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
             
               batch.begin();    
               for (int i=0; i<squareObjList.size(); i++){
                   Square currentSquare=squareObjList.get(i);
                   Sprite drawSquare = spriteSquareList.get(i);
                   drawSquare.setPosition(currentSquare.getX(), currentSquare.getY());
                   
                   drawSquare.draw(batch);
;               }
               batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
