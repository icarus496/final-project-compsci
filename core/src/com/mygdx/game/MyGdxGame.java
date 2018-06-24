/** This is the chess game. The things I couldn't get working (well, that I didn't have time to get working) 
 * were 1. Castling, 2. pawn promotion, and 3. checkmate. Check still works reasonably well, some kinks haven't been worked out yet, but a king can't 
 * move into check, and if he is in check, the king must be moved. This is true for both the AI and human player. The game crashes when the black
 * team is put in checkmate or stalemate because it tries to index an empty list. Other than those things, everything else is fully functional,
 * so if you want to play chess against a (really really bad) AI, this will work, provided you keep track of who won. I could have made it two-player, and
 * that probably would have been easier, but I figured a random AI would be more interesting.
**/

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import java.lang.Math;
import java.lang.Thread;
import java.util.Collections;
import java.util.Random;


public class MyGdxGame extends ApplicationAdapter {
        Piece selectedPiece; //the piece the user has selected when taking user input
        SpriteBatch batch; //for the renderer
        int squareSize=640/8; //setting size of chessboard squares
        int rowNum=0;
        int squareX = 0; //variable to hold square x coordiant
        int mouseClicked=0; //quickfix for a problem with click detection
        int n=0; //ensuring that a square changes color the right way
        int turnNumber = 1; //keeping track of how many turns it's been
        int lastTurnNumber = 0; //most recent turn
        int legalMovesShown=0; //is the game displaying the highlited squares
        int kingIndex;
        //All variable names here are self explanatory
        ArrayList <Pawn> pawnList = new ArrayList();
        ArrayList <Knight> knightList = new ArrayList();
        ArrayList <Bishop> bishopList = new ArrayList();
        ArrayList <Rook> rookList = new ArrayList();
        ArrayList <Queen> queenList = new ArrayList();
        ArrayList <King> kingList = new ArrayList();
        ArrayList <Square> squareObjList = new ArrayList();
        ArrayList <Sprite> spriteSquareList = new ArrayList();
        ArrayList <Square> moveSquares = new ArrayList();
        ArrayList <Piece> pieceList = new ArrayList();
        ArrayList <Sprite> knightSpriteList = new ArrayList();
        ArrayList <Sprite> pawnSpriteList = new ArrayList();
        ArrayList <Sprite> bishopSpriteList = new ArrayList();
        ArrayList <Sprite> rookSpriteList = new ArrayList();
        ArrayList <Sprite> queenSpriteList = new ArrayList();
        ArrayList <Sprite> kingSpriteList = new ArrayList();
        ArrayList <Square> legalMoveSquares = new ArrayList(); //list of squares that can be moved to under the rules of chess
        
        
        
        
        
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
         int checkSquareXY(int x, int y){  //returns 1 is the square is at that position, 0 if not.
              if ((this.xRow == x) && (this.yRow == y)){
                   return 1;
              }
              else{
                   return 0;
              }
         }
         Sprite getSpriteSquare(){ //creates a libGDX sprite for that square, at that square's x and y.
             Texture squareImg; 
             Sprite squareSprite;
             Pixmap renderSquare = new Pixmap(640/8, 640/8, Pixmap.Format.RGBA8888);
              if (this.color==0){
                   renderSquare.setColor(Color.WHITE);
              }
              if (this.color == 1){
                   renderSquare.setColor(Color.RED);
              }
              if (this.color == 2){
                  renderSquare.setColor(Color.YELLOW);
              }
              renderSquare.fill();
              squareImg = new Texture(renderSquare);
              squareSprite = new Sprite(squareImg);
              squareSprite.setPosition(this.squareX, this.squareY);
              return squareSprite;
         }
    }
    
        
        
        
    class Piece  //superclass piece, all chesspieces are a subset of this
    {
         int pieceX;
         int pieceY;
         int[] currSquare;
         int pieceColor; //white=1, black=0
         ArrayList<int[]> legalMoves;
         Piece(int pieceX, int pieceY, int pieceColor){
              this.pieceX=pieceX;
              this.pieceY=pieceY;
              this.currSquare = new int[2]; //the piece's current square
              this.currSquare[0]=pieceX; 
              this.currSquare[1]=pieceY;
              this.legalMoves = new ArrayList();
              this.pieceColor = pieceColor;
              
         }
         int getColor(){
             return this.pieceColor;
         }
         int getPieceX(){
              return this.pieceX;
         }
         int getPieceY(){
              return this.pieceY;
         }
         int isPieceAt(int x, int y){ //is the piece at x and y coordinates, 1 if yes, 0 if no
              if ((pieceX == x) && (pieceY==y)){
                   return 1;
              }
              else{
                   return 0;
              }
         }
        void setPos(int x, int y){
             this.pieceX = x;
             this.pieceY = y;
         }
        ArrayList <int[]> getLegalMoves(){
            return this.legalMoves;
        }
    }
    
    
    
    
    class Pawn extends Piece
    {
         Pawn(int pieceX, int pieceY, int pieceColor){
              super(pieceX, pieceY, pieceColor);
              this.pieceX=pieceX; //1 to 8
              this.pieceY=pieceY; //1 to 8
              this.pieceColor=pieceColor; //white = 0 black = 1
         }
         Sprite getSprite(){
              Texture pawnTexture = new Texture("pawn_w.png");
              Sprite thisPawn = new Sprite(pawnTexture);
              thisPawn.setPosition((this.pieceX*80)-80,(this.pieceY*80)-80); //changing object x and y into rendering x and y
              if (this.pieceColor==1){
                thisPawn.setColor(1,1,1,1);
              }
              else if (this.pieceColor==0){
                  thisPawn.setColor(0,0,0,1);
              }
              return thisPawn;

         }
         void createLegalMoves(){
             this.legalMoves = new ArrayList();
             if (this.pieceColor == 0){ //black
                 int newY = this.pieceY - 1;
                 int newX = this.pieceX;
                 int firstTurnY = 5;
                 int[] move1 = {newX, newY};
                 int[] move3 = {newX, firstTurnY};
                 if (this.pieceY == 7)
                 {
                      legalMoves.add(move3);
                 }
                 this.legalMoves.add(move1);
                 for (int i = 0; i < pieceList.size();i++){
                     Piece currentPiece=pieceList.get(i);
                     if ((this.pieceY-1==currentPiece.getPieceY()) && (this.pieceX-1 == currentPiece.getPieceX())){
                         int[] move2 = {this.pieceX-1,this.pieceY-1};
                         this.legalMoves.add(move2);
                     }
                     if ((this.pieceY-1 == currentPiece.getPieceY()) && (this.pieceX+1 == currentPiece.getPieceX())){
                         int[] move2 = {this.pieceX+1, this.pieceY-1};
                         this.legalMoves.add(move2);
                     }
                     if ((newY == currentPiece.getPieceY()) && newX == currentPiece.getPieceX()){
                         this.legalMoves.remove(move1);
                         this.legalMoves.remove(move3);
                     }
                     if (5 == currentPiece.getPieceY() && newX == currentPiece.getPieceX()){
                          this.legalMoves.remove(move3);
                     }
                 }
             }
             if (this.pieceColor == 1){ //white
                 int newY = this.pieceY + 1;
                 int newX = this.pieceX;
                 int firstTurnY = this.pieceY+2;
                 int[] move1 = {newX, newY};
                 int[] move3 = {newX, firstTurnY};
                 this.legalMoves.add(move1);
                 if (this.pieceY ==2){
                    this.legalMoves.add(move3);
                 }
                 for (int i = 0; i < pieceList.size();i++){ //looking through the list of pieces for a friendly piece in the way
                     Piece currentPiece=pieceList.get(i);
                     if ((this.pieceY+1==currentPiece.getPieceY()) && (this.pieceX-1 == currentPiece.getPieceX())){
                         int[] move2 = {this.pieceX-1,this.pieceY+1};
                         this.legalMoves.add(move2);
                     }
                     if ((this.pieceY+1 == currentPiece.getPieceY()) && (this.pieceX+1 == currentPiece.getPieceX())){
                         int[] move2 = {this.pieceX+1, this.pieceY+1};
                         this.legalMoves.add(move2);
                     }
                     if ((newY == currentPiece.getPieceY()) && newX == currentPiece.getPieceX()){
                         this.legalMoves.remove(move1);
                         this.legalMoves.remove(move3);
                     }
                     if (newY >=9 || newX >=9){
                         this.legalMoves.remove(move1);
                     }
                     if (4 == currentPiece.getPieceY() && newX == currentPiece.getPieceX()){
                          this.legalMoves.remove(move3);
                     }
                     
                   }    
                 }

             }
         }
    class Knight extends Piece
    {
         Knight(int pieceX, int pieceY, int pieceColor){
              super(pieceX, pieceY, pieceColor);
              this.pieceX=pieceX; //1 to 8
              this.pieceY=pieceY; //1 to 8
              this.pieceColor=pieceColor; //white = 0 black = 1
         }
         Sprite getSprite(){
              Texture KnightTexture = new Texture("knight_w.png");
              Sprite thisPiece = new Sprite(KnightTexture);
              thisPiece.setPosition((this.pieceX*80)-80,(this.pieceY*80)-80); //changing object x and y into rendering x and y
              if (this.pieceColor==1){
                thisPiece.setColor(1,1,1,1);
              }
              else if (this.pieceColor==0){
                  thisPiece.setColor(0,0,0,1);
              }
              return thisPiece;

         }
         void createLegalMoves(){
             this.legalMoves = new ArrayList();
             int[] move1 = {this.pieceX+1, this.pieceY+2};
             int[] move2 = {this.pieceX+2, this.pieceY-1};
             int[] move3 = {this.pieceX+1, this.pieceY-2};
             int[] move4 = {this.pieceX-2, this.pieceY+1};
             int[] move5 = {this.pieceX-1, this.pieceY+2};
             int[] move6 = {this.pieceX+2, this.pieceY+1};
             int[] move7 = {this.pieceX-1, this.pieceY-2};
             int[] move8 = {this.pieceX-2, this.pieceY-1};
             int[][] moves = {move1, move2, move3, move4, move5, move6, move7, move8};
             Collections.addAll(this.legalMoves, moves);
             for (int i = 0; i<8; i++){
                 if (moves[i][0] >=9 || moves[i][1] >=9){
                     legalMoves.remove(moves[i]);
                 }
             }
         }
    }
    class Bishop extends Piece
    {
        Bishop(int pieceX, int pieceY, int pieceColor){
              super(pieceX, pieceY, pieceColor);
              this.pieceX=pieceX; //1 to 8
              this.pieceY=pieceY; //1 to 8
              this.pieceColor=pieceColor; //white = 0 black = 1
        }
    Sprite getSprite(){
              Texture Texture = new Texture("bishop_w.png");
              Sprite thisPiece = new Sprite(Texture);
              thisPiece.setPosition((this.pieceX*80)-80,(this.pieceY*80)-80); //changing object x and y into rendering x and y
              if (this.pieceColor==1){
                thisPiece.setColor(1,1,1,1);
              }
              else if (this.pieceColor==0){
                  thisPiece.setColor(0,0,0,1);
              }
              return thisPiece;

         }
         void createLegalMoves(){
             this.legalMoves = new ArrayList<int[]>();
             int newX = this.pieceX+1;
             int newY = this.pieceY+1;
             up_to_left:
                while (newX<9 && newY<9){
                   for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break up_to_left;
                       }

                   }
                   int[] xyCoOrds = new int[2];
                   xyCoOrds[0]=newX;
                   xyCoOrds[1]=newY;
                   legalMoves.add(xyCoOrds);
                   for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break up_to_left;
                       }

                   }
                   newX++;
                   newY++;
                }
             newX=this.pieceX-1;
             newY=this.pieceY+1;
             up_to_right:
                while (newX>0 && newY<9){
                   for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break up_to_right;
                       }
                   }
                   int[] xyCoOrds = new int[2];
                   xyCoOrds[0]=newX;
                   xyCoOrds[1]=newY;
                   legalMoves.add(xyCoOrds);
                   for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break up_to_right;
                       }

                   }
                   newX--;
                   newY++;
                }
             newX=this.pieceX-1;
             newY=this.pieceY-1;
             down_to_left:
                while (newX>0 && newY>0){
                   for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY &&checkedPiece.getColor() == this.pieceColor){
                           break down_to_left;
                       }
                   }
                   int[] xyCoOrds = new int[2];
                   xyCoOrds[0]=newX;
                   xyCoOrds[1]=newY;
                   legalMoves.add(xyCoOrds);
                   for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break down_to_left;
                       }

                   }
                   newX--;
                   newY--;
                }
             newX=this.pieceX+1;
             newY=this.pieceY-1;
             down_to_right:
             while (newX<9 && newY>0){
                 for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY &&checkedPiece.getColor() == this.pieceColor){
                           break down_to_right;
                       }
                 }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break down_to_right;
                       }

                   }
                newX++;
                newY--;
             }
             }
             
             

         }
    class Rook extends Piece
    {
      Rook(int pieceX, int pieceY, int pieceColor){
              super(pieceX, pieceY, pieceColor);
              this.pieceX=pieceX; //1 to 8
              this.pieceY=pieceY; //1 to 8
              this.pieceColor=pieceColor; //white = 0 black = 1
      }
      Sprite getSprite(){
              Texture Texture = new Texture("rook_w.png");
              Sprite thisPiece = new Sprite(Texture);
              thisPiece.setPosition((this.pieceX*80)-80,(this.pieceY*80)-80); //changing object x and y into rendering x and y
              if (this.pieceColor==1){
                thisPiece.setColor(1,1,1,1);
              }
              else if (this.pieceColor==0){
                  thisPiece.setColor(0,0,0,1);
              }
              return thisPiece;

         }
         void createLegalMoves(){
             this.legalMoves = new ArrayList<int[]>();
             int newX = this.pieceX+1;
             int newY = this.pieceY;
             right:
             while (newX<9){
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break right;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break right;
                       }

                   }
                newX++;
             }
             newX=this.pieceX-1;
             newY=this.pieceY;
             left:
             while (newX>0){
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break left;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break left;
                       }

                   }
                newX--;
                 }
             newX=this.pieceX;
             newY=this.pieceY-1;
             down:
             while (newY>0){
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break down;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break down;
                       }

                   }
                newY--;
             }
             newY=this.pieceY+1;
             up:
             while (newY<9){
             for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break up;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break up;
                       }

                   }
                newY++;
             }
             }
             
    }
    class Queen extends Piece
    {
        Queen(int pieceX, int pieceY, int pieceColor){
              super(pieceX, pieceY, pieceColor);
              this.pieceX=pieceX; //1 to 8
              this.pieceY=pieceY; //1 to 8
              this.pieceColor=pieceColor; //white = 0 black = 1
        }
        Sprite getSprite(){
              Texture Texture = new Texture("queen_w.png");
              Sprite thisPiece = new Sprite(Texture);
              thisPiece.setPosition((this.pieceX*80)-80,(this.pieceY*80)-80); //changing object coordiante system into libGDX native coordinate system
              if (this.pieceColor==1){
                thisPiece.setColor(1,1,1,1);
              }
              else if (this.pieceColor==0){
                  thisPiece.setColor(0,0,0,1);
              }
              return thisPiece;

         }
         void createLegalMoves(){
             this.legalMoves = new ArrayList<int[]>();
             int newX = this.pieceX+1;
             int newY = this.pieceY;
             right:
             while (newX<=8){
                //I do this next bit over and over, it's just to exclude squares that the piece can't move to. Yes, I should have made it a method, but I didn't realise that till now  
                for (int i=0; i<pieceList.size(); i++){ 
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break right;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break right;
                       }

                   }
                newX++;
             }
             newX=this.pieceX-1;
             newY=this.pieceY;
             left:
             while (newX>0){
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break left;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                      for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break left;
                       }

                   }
                newX--;
                 }
             newX=this.pieceX;
             newY=this.pieceY-1;
             down:
             while (newY>0){
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break down;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break down;
                       }

                   }
                newY--;
             }
             newY=this.pieceY+1;
             up:
             while (newY<=9){
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break up;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break up;
                       }

                   }
                newY++;  
             }
             newX = this.pieceX+1;
             newY = this.pieceY+1;
             upLeft:
             while (newX<=8 && newY<=8){
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break upLeft;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break upLeft;
                       }

                   }
                newX++;
                newY++;
             }
             newX=this.pieceX-1;
             newY=this.pieceY+1;
             upRight:
             while (newX>0 && newY<=8){
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break upRight;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break upRight;
                       }

                   }
                newX--;
                newY++;
                 }
             newX=this.pieceX-1;
             newY=this.pieceY-1;
             downLeft:
             while (newX>0 && newY>0){
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break downLeft;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break downLeft;
                       }

                   }
                newX--;
                newY--;
             }
             newX=this.pieceX+1;
             newY=this.pieceY-1;
             downRight:
             while (newX<=8 && newY>0){
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() == this.pieceColor){
                           break downRight;
                       }

                   }
                int[] xyCoOrds = new int[2];
                xyCoOrds[0]=newX;
                xyCoOrds[1]=newY;
                legalMoves.add(xyCoOrds);
                for (int i=0; i<pieceList.size(); i++){
                       Piece checkedPiece = pieceList.get(i);
                       if (checkedPiece.getPieceX() == newX && checkedPiece.getPieceY() == newY && checkedPiece.getColor() != this.pieceColor){
                           break downRight;
                       }

                   }
                newX++;
                newY--;
             }
             }
    }
    class King extends Piece
    {
        int inCheck = 0;
        King(int pieceX, int pieceY, int pieceColor){
              super(pieceX, pieceY, pieceColor);
              this.pieceX=pieceX; //1 to 8
              this.pieceY=pieceY; //1 to 8
              this.pieceColor=pieceColor; //white = 0 black = 1
        }
        Sprite getSprite(){
              Texture Texture = new Texture("king_w.png");
              Sprite thisPiece = new Sprite(Texture);
              thisPiece.setPosition((this.pieceX*80)-80,(this.pieceY*80)-80); //changing object x and y into rendering x and y
              if (this.pieceColor==1){
                thisPiece.setColor(1,1,1,1);
              }
              else if (this.pieceColor==0){
                  thisPiece.setColor(0,0,0,1);
              }
              return thisPiece;

         }
         void createLegalMoves(){
             this.legalMoves = new ArrayList<int[]>();
             int[] move1 = {this.pieceX,this.pieceY+1};
             int[] move2 = {this.pieceX+1,this.pieceY+1};
             int[] move3 = {this.pieceX+1,this.pieceY};
             int[] move4 = {this.pieceX+1,this.pieceY-1};
             int[] move5 = {this.pieceX,this.pieceY-1};
             int[] move6 = {this.pieceX-1,this.pieceY-1};
             int[] move7 = {this.pieceX-1,this.pieceY};
             int[] move8 = {this.pieceX-1,this.pieceY+1};
             int[][] moves ={move1, move2, move3, move4, move5, move6, move7, move8};
             Collections.addAll(legalMoves,moves);  
             for (int i = 0; i<8; i++){ //looping through the moves preventing the king from moving offscreen
                 if (moves[i][0] >=9 || moves[i][1] >=9){
                     legalMoves.remove(moves[i]);
                 }
             }    
         }
         }

        int getCursorX(){
            return (Gdx.input.getX()/80)+1;
        }
        
        int getCursorY(){
            return ((Math.abs(Gdx.input.getY()-640)/80)+1);
        }
        int getInCheck(int x, int y, King king){  //bug that I can't figure out makes this crash the program on occasion. I've spent hours trying to debug it
            /**essentially, this tells you if the king would be in check on a given square. It moves the king, checks to see if it is in check, and 
             then moves it back, returning the result. 1 means yes, 0 means no.**/
            int oldX = king.getPieceX();
            int oldY = king.getPieceY();
            king.setPos(x,y);
            System.out.println("Point 1: " + pieceList.size()); //for debugging
            for (int i = 0; i<pieceList.size(); i++){
               Piece currentPiece = pieceList.get(i);
               //generating an updated list of legal moves
               if (currentPiece instanceof Pawn) {
                     ((Pawn)currentPiece).createLegalMoves();
               }
               if (currentPiece instanceof Knight) {
                     ((Knight)currentPiece).createLegalMoves();
               }
               if (currentPiece instanceof Bishop) {
                     ((Bishop)currentPiece).createLegalMoves();
               }
               if (currentPiece instanceof Rook) {
                     ((Rook)currentPiece).createLegalMoves();
               }
               if (currentPiece instanceof Queen) {
                    ((Queen)currentPiece).createLegalMoves();
               }
                ArrayList <int[]> currentLegalMoves = currentPiece.getLegalMoves();
                for (int j = 0; j<currentLegalMoves.size(); j++){
                      if (currentPiece.getColor() != king.getColor() && king.isPieceAt(currentLegalMoves.get(j)[0], currentLegalMoves.get(j)[1]) == 1){
                        king.setPos(oldX,oldY);
                        System.out.println("Point 2: " + pieceList.size()); //for debugging
                        return 1;
                    }
                    else{
                        continue;
                    }
                }
            }
            king.setPos(oldX, oldY);
            System.out.println("Point 3: " + pieceList.size());
            return 0;
        }
        
        @Override
        public void create () { 
                batch = new SpriteBatch();
                for(int n = 1; n <= 64; n++){ //creates an 8 by 8 grid of square objects
                     int squareColor;
                     if (rowNum%2==0)  //figuring out the color of the square
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
                for (int i=0; i<squareObjList.size(); i++){    
                    spriteSquareList.add(squareObjList.get(i).getSpriteSquare());  
                } 
                //defining all the chess peices
                pawnList.add(new Pawn(1,2,1));
                pawnList.add(new Pawn(2,2,1));
                pawnList.add(new Pawn(3,2,1));
                pawnList.add(new Pawn(4,2,1));
                pawnList.add(new Pawn(5,2,1));
                pawnList.add(new Pawn(6,2,1));
                pawnList.add(new Pawn(7,2,1));
                pawnList.add(new Pawn(8,2,1));
                pawnList.add(new Pawn(1,7,0));
                pawnList.add(new Pawn(2,7,0));
                pawnList.add(new Pawn(3,7,0));
                pawnList.add(new Pawn(4,7,0));
                pawnList.add(new Pawn(5,7,0));
                pawnList.add(new Pawn(6,7,0));
                pawnList.add(new Pawn(7,7,0));
                pawnList.add(new Pawn(8,7,0));
                pieceList.addAll(pawnList);
                knightList.add(new Knight(2,1,1));
                knightList.add(new Knight(7,1,1));
                knightList.add(new Knight(2,8,0));
                knightList.add(new Knight(7,8,0));
                pieceList.addAll(knightList);
                bishopList.add(new Bishop(3,1,1));
                bishopList.add(new Bishop(6,1,1));
                bishopList.add(new Bishop(3,8,0));
                bishopList.add(new Bishop(6,8,0));
                pieceList.addAll(bishopList);
                rookList.add(new Rook(1,1,1));
                rookList.add(new Rook(8,1,1));
                rookList.add(new Rook(1,8,0));
                rookList.add(new Rook(8,8,0));
                pieceList.addAll(rookList);
                queenList.add(new Queen(4,1,1));
                queenList.add(new Queen(4,8,0));
                pieceList.addAll(queenList);
                kingList.add(new King(5,1,1));
                kingList.add(new King(5,8,0));
                pieceList.addAll(kingList);
            //generating the spritelists to draw the pieces
            for (int i=0; i<pawnList.size(); i++){
                 pawnSpriteList.add(pawnList.get(i).getSprite());
            }   
            for (int i = 0; i<knightList.size(); i++){
                knightSpriteList.add(knightList.get(i).getSprite());
            }
            for (int i = 0; i<bishopList.size(); i++){
                bishopSpriteList.add(bishopList.get(i).getSprite());
            }
            for (int i = 0; i<rookList.size(); i++){
                rookSpriteList.add(rookList.get(i).getSprite());
            }
            for (int i =0; i<queenList.size(); i++){
                queenSpriteList.add(queenList.get(i).getSprite());
            }
            for (int i=0; i<kingList.size(); i++){
                kingSpriteList.add(kingList.get(i).getSprite());
            }
	}

	@Override
	public void render () { //this is run repeatedly by LibGDX, it draws graphics to the screen
               Gdx.gl.glClearColor(1, 0, 0, 1);
               Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
               if (turnNumber > lastTurnNumber){ //reloading the legal moves for each piece
               for (int i=0; i<knightList.size(); i++){
                   knightList.get(i).createLegalMoves();
               }
               for (int i=0; i<pawnList.size(); i++){
                   pawnList.get(i).createLegalMoves();
               }
               for (int i=0; i<bishopList.size(); i++){
                   bishopList.get(i).createLegalMoves();
               }
               for (int i=0; i<rookList.size(); i++){
                   rookList.get(i).createLegalMoves();
               }
                
               for (int i=0; i<queenList.size(); i++){
                   queenList.get(i).createLegalMoves();
               }
               for (int i=0; i<kingList.size(); i++){
                   kingList.get(i).createLegalMoves();
               }
               lastTurnNumber++; //incrementing the previous turn's number by 1
               }
               if (turnNumber%2 == 0){ //Handling the Ai's actions
                   //this is all defining variables to be used down below. They just hold moves that can be made and determine if the list of moves is final.
                   Piece pieceToMove = null;
                    ArrayList <int[]> availableMoves = new ArrayList();
                    int listConfirmed = 0;
                    int moveConfirmed = 0;
                    int selectedMove = 0; 
                    ArrayList <Piece> blackPieces = new ArrayList();
                    //creating a list of only black pieces
                    for(int i = 0; i < pieceList.size(); i++){
                         if (pieceList.get(i).getColor() == 0){
                              blackPieces.add(pieceList.get(i));
                              }
                         }
                    //looping till a valid list of moves is generated (non-empty)
                    while(listConfirmed == 0){
                    if (getInCheck(kingList.get(1).getPieceX(),kingList.get(1).getPieceY(), kingList.get(1)) == 1){
                        pieceToMove = kingList.get(1);
                    }
                    else{
                        int selectPiece = (int)(Math.random() * blackPieces.size());  //generating the random piece to use
                        pieceToMove = blackPieces.get(selectPiece);
                    }
                    availableMoves.addAll(pieceToMove.getLegalMoves()); //creating a list of moves for the chosen piece
                    overlapChecker:
                    while (true) {
                    for (int j = 0; j<availableMoves.size(); j++){ //looping through each move to get rid of bad ones
                        if (pieceToMove instanceof King){
                           if (getInCheck(availableMoves.get(j)[0], availableMoves.get(j)[1], (King) pieceToMove) == 1){
                              availableMoves.remove(j);
                              continue overlapChecker;
                              }
                        }
                        for (int k = 0; k<blackPieces.size(); k++){ //looping throug each piece to avoid overlap to reduce the list of available moves to non-overlapping ones
                            if (j == availableMoves.size()){
                                continue overlapChecker;
                            } 
                            if (k == blackPieces.size()){
                                continue overlapChecker;
                            }
                            if ((blackPieces.get(k).isPieceAt(availableMoves.get(j)[0], availableMoves.get(j)[1]) == 1)
                                    || (availableMoves.get(j)[0] == kingList.get(0).getPieceX() && availableMoves.get(j)[1] == kingList.get(0).getPieceY())){ 
                                //checking for overlap with friendlies or with the enemy king. You can't take the enemy king.
                                availableMoves.remove(j);
                                continue overlapChecker;
                            
                            }
                            else{
                                continue;
                            }
                          
                        }
                    }
                    break;
                    }
                    if (availableMoves.isEmpty()){ //checking if the list is empty. If so, start over.
                        if (getInCheck(kingList.get(1).getPieceX(),kingList.get(1).getPieceY(), kingList.get(1)) == 1){ //checks if white has won. Not fully debugged yet
                            break;
                        } 
                        continue;
                    }
                    else{
                         listConfirmed = 1; //finish the loop, release the list of moves
                    }   
                    }
                   pieceToMove.setPos(availableMoves.get(selectedMove)[0], availableMoves.get(selectedMove)[1]);
                   for (int j = 0; j<pieceList.size(); j++){ //looping through the list of pieces to see if black took a piece
                        Piece currentPiece = pieceList.get(j);
                        if (currentPiece.getPieceX() == availableMoves.get(selectedMove)[0] //checking if black has taken a piece
                                && currentPiece.getPieceY() == availableMoves.get(selectedMove)[1] 
                                && currentPiece.getColor() == 1){
                                    pieceList.remove(j);
                                    if (currentPiece instanceof Pawn) {
                                         pawnSpriteList.remove(pawnList.indexOf(((Pawn)currentPiece))); //removes the piece from the spritelist
                                         pawnList.remove(pawnList.indexOf((Pawn)currentPiece)); //removes the pawn from the pawnlist
                                    }
                                    if (currentPiece instanceof Knight) {
                                         knightSpriteList.remove(knightList.indexOf(((Knight)currentPiece))); //removes the piece from the spritelist
                                         knightList.remove(knightList.indexOf((Knight)currentPiece)); //removes the knight from the pawnlist
                                    }
                                    if (currentPiece instanceof Bishop) {
                                         bishopSpriteList.remove(bishopList.indexOf(((Bishop)currentPiece))); //removes the piece from the spritelist
                                         bishopList.remove(bishopList.indexOf((Bishop)currentPiece)); //removes the bishop from the list
                                    }
                                    if (currentPiece instanceof Rook) {
                                         rookSpriteList.remove(rookList.indexOf(((Rook)currentPiece))); //removes the piece from the spritelist
                                         rookList.remove(rookList.indexOf((Rook)currentPiece)); //removes the rook from the list
                                    }
                                    if (currentPiece instanceof Queen) {
                                         queenSpriteList.remove(queenList.indexOf(((Queen)currentPiece))); //removes the piece from the spritelist
                                         queenList.remove(queenList.indexOf((Queen)currentPiece)); //removes the queen from the list
                                    }
                        } 
                    } 
                    if (pieceToMove instanceof Pawn){ //updating the sprites of the piece objects so they can be drawn in the new position
                         int index = pawnList.indexOf(pieceToMove);
                         pawnSpriteList.set(index, ((Pawn)pieceToMove).getSprite());
                    }
                    if (pieceToMove instanceof Bishop) {
                         int index = bishopList.indexOf(pieceToMove);
                         bishopSpriteList.set(index, ((Bishop)pieceToMove).getSprite());
                    }
                    if (pieceToMove instanceof Knight) {
                         int index = knightList.indexOf(pieceToMove);
                         knightSpriteList.set(index, ((Knight)pieceToMove).getSprite());
                    }
                    if (pieceToMove instanceof Rook) {
                          int index = rookList.indexOf(pieceToMove);
                          rookSpriteList.set(index, ((Rook)pieceToMove).getSprite());
                    }
                    if (pieceToMove instanceof Queen) {
                          int index = queenList.indexOf(pieceToMove);
                          queenSpriteList.set(index, ((Queen)pieceToMove).getSprite());
                    }
                    if (pieceToMove instanceof King) {
                          int index = kingList.indexOf(pieceToMove);
                          kingSpriteList.set(index, ((King)pieceToMove).getSprite());
                    }
                    turnNumber++;
                         
                         
               }
               else if (turnNumber%2 == 1){ //handling the player's actions
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){ //checking if mouse is clicked
                    try        //waiting a bit so it doesn't register two clicks
                    {
                        Thread.sleep(100);
                    } 
                        catch(InterruptedException ex) 
                    {
                        Thread.currentThread().interrupt();
                    }
                    mouseClicked = 1;
                    }
                if (mouseClicked == 1 && legalMovesShown==0){ //checking if mouse is clicked and ensuring that legal moves haven't already been shown on screen
                    try        //waiting a bit so it doesn't register two clicks
                    {
                        Thread.sleep(100);
                    } 
                        catch(InterruptedException ex) 
                    {
                        Thread.currentThread().interrupt();
                    }
                    for (int i=0; i<pieceList.size(); i++){ //looping through the list of pieces
                        Piece currentPiece = pieceList.get(i); 
                        if (currentPiece.isPieceAt(getCursorX(), getCursorY()) == 1 && (currentPiece.getColor() == 1)) //Checking if a piece was clicked and that the king was not in check
                        {
                        selectedPiece = currentPiece;
                        if (getInCheck(kingList.get(0).getPieceX(), kingList.get(0).getPieceY(), kingList.get(0)) == 1){ //Checking if the king is in check
                            selectedPiece = kingList.get(0);
                        }
                            for (int j=0; j<selectedPiece.getLegalMoves().size(); j++){ //looping through legal moves
                                int[] currentMove = selectedPiece.getLegalMoves().get(j); //gets the 2 element array containing the legal move
                                squareLoop: //label to allow inner loop to control outer loop
                                for (int k=0; k<squareObjList.size(); k++){ //looking for the squares that corresponds to a legal move
                                    Square currentSquare = squareObjList.get(k);
                                    int currentSquareX = currentSquare.getxRow();
                                    int currentSquareY = currentSquare.getyRow();
                                    if (currentSquare.checkSquareXY(currentMove[0], currentMove[1]) == 1){ //finding the square that corresponds to a legal move's coordinates
                                        if (selectedPiece instanceof King){ //should not color in a square if it would make the king be in check
                                            if (getInCheck(currentSquareX, currentSquareY, (King)selectedPiece) == 1){
                                            continue squareLoop;
                                            }

                                        }
                                        for (int l=0; l<pieceList.size(); l++){ //looping through piecelist again to look for a friendly piece in the line of fire
                                             Piece thisPiece = pieceList.get(l);
                                             if ((thisPiece.isPieceAt(currentSquareX,currentSquareY) == 1) && (thisPiece.getColor() == currentPiece.getColor())
                                                || (kingList.get(1).isPieceAt(currentSquareX, currentSquareY) == 1)){ //not counting that square as a legal move if it attacks a friendly or a king
                                                 continue squareLoop;
                                             }
                                        }
                                                  currentSquare.setColor(2);
                                                  spriteSquareList.set(k,currentSquare.getSpriteSquare()); //turning the square with a legal move yellow
                                                  legalMoveSquares.add(currentSquare);
                                                  legalMovesShown = 1;
                                                  mouseClicked = 0;
                                    }
                                }
                            }

                        }

                    }
                }
                else if ((mouseClicked == 1) && (legalMovesShown == 1)){ //checking if legal moves have already been shown on screen
                    legalMovesShown = 0;
                    for (int i = 0; i<legalMoveSquares.size(); i++){ //looping through the list of squares corresponding to legal moves
                        Square currentSquare = legalMoveSquares.get(i);
                        int currentX = currentSquare.getxRow();
                        int currentY = currentSquare.getyRow();
                        if (currentX == getCursorX() && currentY == getCursorY()){ //checking if you clicked on a highlighted square
                            for (int j=0; j<pieceList.size(); j++){ //checking if there's an enemy piece on the square you clicked. This is what allows you to take pieces. 
                                Piece checkingPiece = pieceList.get(j);
                                if ((checkingPiece.isPieceAt(currentX, currentY) == 1) && (checkingPiece.getColor() != selectedPiece.getColor())){ //if there's a piece at the square we're checking on
                                    if (checkingPiece instanceof Pawn) {
                                         pawnSpriteList.remove(pawnList.indexOf(((Pawn)checkingPiece))); //removes the piece from the spritelist
                                         pawnList.remove(pawnList.indexOf((Pawn)checkingPiece)); //removes the pawn from the pawnlist
                                    }
                                    if (checkingPiece instanceof Knight) {
                                         knightSpriteList.remove(knightList.indexOf(((Knight)checkingPiece))); //removes the piece from the spritelist
                                         knightList.remove(knightList.indexOf((Knight)checkingPiece)); //removes the knight from the pawnlist
                                    }
                                    if (checkingPiece instanceof Bishop) {
                                         bishopSpriteList.remove(bishopList.indexOf(((Bishop)checkingPiece))); //removes the piece from the spritelist
                                         bishopList.remove(bishopList.indexOf((Bishop)checkingPiece)); //removes the bishop from the list
                                    }
                                    if (checkingPiece instanceof Rook) {
                                         rookSpriteList.remove(rookList.indexOf(((Rook)checkingPiece))); //removes the piece from the spritelist
                                         rookList.remove(rookList.indexOf((Rook)checkingPiece)); //removes the rook from the list
                                    }
                                    if (checkingPiece instanceof Queen) {
                                         queenSpriteList.remove(queenList.indexOf(((Queen)checkingPiece))); //removes the piece from the spritelist
                                         queenList.remove(queenList.indexOf((Queen)checkingPiece)); //removes the queen from the list
                                    }
                                    

                                   pieceList.remove(j);
                                   continue;
                                }
                            }
                            selectedPiece.setPos(getCursorX(), getCursorY());
                            if (selectedPiece instanceof Pawn) {
                                int index = pawnList.indexOf((Pawn)selectedPiece); //finds the index of the selected piece. the piece lists and their respective sprite lists are indexed the same way
                                pawnSpriteList.set(index, ((Pawn)selectedPiece).getSprite()); //changes the spritelist so the piece is drawn at its new location
                            }
                            if (selectedPiece instanceof Knight){
                                int index = knightList.indexOf((Knight)selectedPiece);
                                knightSpriteList.set(index,((Knight)selectedPiece).getSprite());
                            }
                            if (selectedPiece instanceof Bishop){
                                int index = bishopList.indexOf((Bishop)selectedPiece);
                                bishopSpriteList.set(index,((Bishop)selectedPiece).getSprite());
                            }
                            if (selectedPiece instanceof Rook){
                                int index = rookList.indexOf((Rook)selectedPiece);
                                rookSpriteList.set(index,((Rook)selectedPiece).getSprite());
                            }
                            if (selectedPiece instanceof Queen){
                                int index = queenList.indexOf((Queen)selectedPiece);
                                queenSpriteList.set(index,((Queen)selectedPiece).getSprite());
                            }
                            if (selectedPiece instanceof King){
                                int index = kingList.indexOf((King)selectedPiece);
                                kingSpriteList.set(index,((King)selectedPiece).getSprite());
                            }
                        turnNumber++;
                        }
                        if (legalMovesShown == 0){ //resetting square colors
                            if (currentSquare.getyRow() % 2 == 0) { //if row is even
                                if (currentSquare.getxRow() % 2 == 0) {currentSquare.setColor(1);} //if column is odd make it red
                                else {currentSquare.setColor(0);}
                            }
                            else{ //if row is odd
                                if (currentSquare.getxRow() % 2 == 0) {currentSquare.setColor(0);} //if column is even make the square white
                                else {currentSquare.setColor(1);}
                            }
                            int squareIndex = squareObjList.indexOf(currentSquare); //finding the index of the selected square
                            spriteSquareList.set(squareIndex, currentSquare.getSpriteSquare()); //adding the changed square to the list to be drawn
                            mouseClicked = 0;
                        }  
                                       }
                    legalMoveSquares = new ArrayList<Square>(); //clearing legalMoveSquares list
                }
               }
               batch.enableBlending();
               batch.begin();      //this just draws the graphics, nothign special here. I loop through the spritelists and draw everything inside
               for (int i=0; i<squareObjList.size(); i++){
                        Square currentSquare=squareObjList.get(i);
                        Sprite drawSquare = spriteSquareList.get(i);
                        drawSquare.draw(batch);
                    }
               for (int i=0; i<pawnSpriteList.size(); i++){
                   pawnSpriteList.get(i).draw(batch);
               }   
               for (int i = 0; i<knightSpriteList.size(); i++){
                   knightSpriteList.get(i).draw(batch);
               }
               for (int i = 0; i<bishopSpriteList.size(); i++){
                   bishopSpriteList.get(i).draw(batch);
               }
               for (int i = 0; i<rookSpriteList.size(); i++){
                   rookSpriteList.get(i).draw(batch);
               }
               for (int i = 0; i<queenSpriteList.size(); i++){
                   queenSpriteList.get(i).draw(batch);
               }
               for (int i=0; i<kingSpriteList.size(); i++){
                   kingSpriteList.get(i).draw(batch);
               }


               batch.end();

               } 
               

	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
