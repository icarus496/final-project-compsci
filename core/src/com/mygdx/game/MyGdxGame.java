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
     int squareX; //variable holding the x pos of the square for rendering use
     int squareY; //variable holding the y pos of the square for rendering use
     int color; //defining the color of the square 0=white, 1=red
     int xRow; //for locating use
     int yRow; //for locating use
     Square(int squareX, int squareY, int squareColor){
          this.squareY = squareY;
          this.squareX = squareX;
          this.color = squareColor;
          this.xRow = (int) Math.round(squareX/80)+1;
          this.yRow = (int) Math.round(squareY/80)+1;
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
     int getxRow(){
          return this.xRow;
     }
     int getyRow(){
          return this.yRow;
     }
     int checkSquareXY(int x, int y){
          if ((this.xRow == x) && (this.yRow == y)){
               return 1;
          }
          else{
               return 0;
          }
     }
     Pixmap getPixmapSquare(){
          Pixmap renderSquare = new Pixmap(640/8, 640/8, Pixmap.Format.RGBA8888);
          if (this.color==0){
               renderSquare.setColor(Color.WHITE);
          }
          if (this.color == 1){
               renderSquare.setColor(Color.RED);
          }
          renderSquare.fill();
          return renderSquare;
     }
}
class Piece
{
     int pieceX;
     int pieceY;
     int[] currSquare;
     int pieceColor; //white=0, black=1
     Piece(int pieceX, int pieceY, int pieceColor){
          this.pieceX=pieceX;
          this.pieceY=pieceY;
          this.currSquare = new int[2];
          this.currSquare[0]=pieceX;
          this.currSquare[1]=pieceY;
     }
     int getPieceX(){
          return this.pieceX;
     }
     int getPieceY(){
          return this.pieceY;
     }
     int isPieceAt(int x, int y){
          if ((pieceX == x) && (pieceY==y)){
               return 1;
          }
          else{
               return 0;
          }
     }        
}
class Pawn extends Piece{
     Pawn(int pieceX, int pieceY, int pieceColor){
          super(pieceX, pieceY, pieceColor);
          this.pieceX=pieceX; //1 to 8
          this.pieceY=pieceY; //1 to 8
     }
     Sprite getPawnTexture(){
          Texture pawnTexture = new Texture("pawn_w.png");
          Sprite thisPawn = new Sprite(pawnTexture);
          thisPawn.setPosition((this.pieceX*80)-80,(this.pieceY*80)-80);
          thisPawn.setColor(0,0,0,1);
          return thisPawn;
          
     }
     
     
}
public class MyGdxGame extends ApplicationAdapter {
	ArrayList pieceList = new ArrayList();
        Pawn pawnTest= new Pawn(1,2,1);
        SpriteBatch batch;
        Sprite drawAPawn;
        int squareSize=640/8; //setting size of chessboard squares
        ArrayList <Square> squareObjList = new ArrayList();
        Texture pleaseGod;
        ArrayList <Sprite> spriteSquareList = new ArrayList();
        int squareX=0;
        int rowNum=0;
	@Override
	public void create () { 
                batch = new SpriteBatch();
                pleaseGod = new Texture(Gdx.files.internal("king_w.png"));
                drawAPawn= pawnTest.getPawnTexture();
                
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
                     squareObjList.add(new Square(squareX,squareY,squareColor));
                     squareX =squareX+squareSize;
                     if (n%8==0){
                         rowNum++;
                     }
                }

	}

	@Override
	public void render () {
               Gdx.gl.glClearColor(1, 0, 0, 1);
               Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
               for(int i=0; i<squareObjList.size(); i++)
               {
                    Square currentSquare = squareObjList.get(i);
                    Pixmap currentSquareImg = currentSquare.getPixmapSquare();
                    Texture SquareImg = new Texture(currentSquareImg);
                    Sprite squareSprite = new Sprite(SquareImg);
                    spriteSquareList.add(squareSprite);     
                } 
               batch.enableBlending();
               batch.begin();    
                    for (int i=0; i<squareObjList.size(); i++){
                        Square currentSquare=squareObjList.get(i);
                        Sprite drawSquare = spriteSquareList.get(i);
                        drawSquare.setPosition(currentSquare.getX(), currentSquare.getY());
                        drawSquare.draw(batch);
                    }
                    drawAPawn.draw(batch);
                    
               batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
