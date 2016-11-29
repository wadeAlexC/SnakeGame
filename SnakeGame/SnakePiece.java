package Snakev2;

import java.awt.Graphics;
import java.util.Random;

public class SnakePiece extends GameObject{

	//Snake functions like a LinkedList- each link has a parent and a child which mimic each
	//other's actions to behave as one
	private SnakePiece parent;
	private SnakePiece child;
	
	//Size of Snake
	private int size;
	
	Grid grid;
	Square curSq;
	
	Random r;
	
	private boolean gameOverSnake = false;
	
	/*
	 * End Class Variables
	 */
	
	//creates a standard snake piece with no vel, parent, or child
	public SnakePiece(int x, int y, ObjType type, Grid grid) {
		super(x, y, type);
		this.parent = null;
		this.child = null;
		velX = 0;
		velY = 0;
		size = 1;
		this.grid = grid; 
		
		//Fixes new SnakePieces being placed out of bounds
		if(x < 0) x = 0 - x;
		if(y < 0) y = y - x;
		//
		
		this.curSq = grid.squares[x / 16][y / 16];
		curSq.hasSnake = true;
		r = new Random();
	}
	
	/*
	 * End Constructor method
	 */
	
	
	//Tick method for SnakePiece- essentially only calculates one new location for the head, and other Pieces follow previous locations
	public void tick(){
		
		if(this.parent == null){ //only use tick() if the SnakePiece does not have a parent
								 //Should always be null (children are not added to handler.list)
			//System.out.println(this);
			SnakePiece temp = this;
			Square oldSq = temp.curSq; //Save old square so the next piece can use its location
			
			/*
			 * Game Over conditions- the worm reaches the outside of the playing field
			 */
			if((oldSq.x / 16) + temp.velX >= grid.squares.length || (oldSq.x / 16) + temp.velX < 0){
				System.out.println("GAME OVER");
				gameOverSnake = true; //assessed via handler.tick()
				return;
			}
			if((oldSq.y / 16) + temp.velY >= grid.squares[0].length || (oldSq.y / 16) + temp.velY < 0){
				System.out.println("GAME OVER");
				gameOverSnake = true; //assessed via handler.tick()
				return;
			}
			/*
			 * 
			 */
			
			temp.curSq = grid.squares[oldSq.x / 16 + temp.velX][oldSq.y / 16 + temp.velY]; //set snake piece to new square
			oldSq.hasSnake = false; //old square no longer has snake piece
			temp.curSq.hasSnake = true; //new square does have a snake piece
			
			if(temp.child != null){
				temp = temp.child;
				while(temp != null){
					
					Square newOldSq = temp.curSq; //save the old square in "newOldSq"
					temp.curSq = oldSq; //give this Piece the previous Piece's square
					oldSq = newOldSq; //set the oldSq to the Piece's previous location
					
					//manage booleans for Squares
					temp.curSq.hasSnake = true;
					oldSq.hasSnake = false;
					
					//increment
					temp = temp.child;
				}
			}
		}
	}

	public void render(Graphics g) {
		//Has no render- Square object render method covers the render
	}
	
	//getter method for Snake size
	public int getSize(){
		return size;
	}
	
	//Method used when Snake collides with food
	public void eatFood(){
		grid.colorNumber++;
		if(grid.colorNumber >= 6){
			grid.colorNumber = 0;
		}
		SnakePiece temp = this;
		while(temp.child != null){
			temp = temp.child;
		}
		
		int newX = 0;
		int newY = temp.curSq.y;
		
		if(temp.getParent() != null){
			//Calculating where to put the next SnakePiece
			int tempVelX = 0;
			int tempVelY = 0;
			
			//finding velocity of the final SnakePiece to determine new piece placement
			if(temp.curSq.x < temp.getParent().curSq.x){
				tempVelX = 1;
			}
			
			else if(temp.curSq.x > temp.getParent().curSq.x){
				tempVelX = -1;
			}
			
			else if(temp.curSq.y < temp.getParent().curSq.y){
				tempVelY = -1;
			}
			
			else{
				tempVelY = 1;
			}
			
			if(velX < 0){
				newX = temp.curSq.x + 16;
			} else {
				newX = temp.curSq.x;
			}
			
			
			//assigning a new position based on velocities
			if(tempVelX < 0){
				newY = temp.curSq.y;
				newX = temp.curSq.x + 16;
				if(newX < 0) { //making sure the new piece wont be placed out of bounds
					newY = temp.curSq.y + 16;
					newX = temp.curSq.x;
				}
				
			} else if (tempVelX > 0){
				newY = temp.curSq.y;
				newX = temp.curSq.x - 16;
				if(newX > 0){
					newY = temp.curSq.y + 16;
					newX = temp.curSq.x;
				}
				
			} else if (tempVelY < 0){
				newX = temp.curSq.x;
				newY = temp.curSq.y - 16;
				if(newY < 0){
					newX = temp.curSq.x - 16;
					newY = temp.curSq.y;
				}
				
			} else {
				newX = temp.curSq.x;
				newY = temp.curSq.y + 16;
				if(newY > 0){
					newX = temp.curSq.x + 16;
					newY = temp.curSq.y;
				}
			}
			
		} else { //temp has no parent (snake of size 1)
			if(newX == temp.curSq.getX() && newY == temp.curSq.getY()){ //the new square is the same as the previous (results in game over)
				//Makes sure the new X or Y dimension set isn't out of bounds
				if(newX - 16 >= 0){
					newX -= 16;
				} else if (newY - 16 >= 0){
					newY -= 16;
				} else if (newX + 16 < 640){
					newX += 16;
				} else {
					newY += 16;
				}
			}
		}
		
		System.out.println("SQUARE CHOSEN: " + temp.curSq);
		temp.child = new SnakePiece(newX, newY, ObjType.Snake, grid);
		temp.child.parent = temp;
		size++;
	}
	
	public boolean isGameOver(){
		return gameOverSnake;
	}
	
	public SnakePiece getParent(){
		return parent;
	}
	
	public SnakePiece getChild(){
		return child;
	}
	
	//toString method for SnakePiece
	//TODO optional- move to StringBuilder for better comp. efficiency
	public String toString(){
		String s = "";
		if(parent == null){
			SnakePiece temp = this;
			while(temp != null){
				s += "[X: " + temp.curSq.x + ", " + "Y: " + temp.curSq.y + "]";
				temp = temp.child;
			}
		}
		return s;
	}
}
