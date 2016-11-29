package Snakev2;

import java.awt.Graphics;

public class Food extends GameObject{
	
	Grid grid;
	Square curSq;
	
	public Food(int x, int y, ObjType type) {
		super(x, y, type);
	}
	
	public void tick(){
		//nothing - new food generation is handled in Game class constructor
	}

	public void render(Graphics g) {
		//nothing, render is handled by Square render method
	}
	
	//Sets the curSq and updates booleans for current and previous squares
	public void setSqUpdateBool(Square sq){
		if(curSq != null){
			curSq.hasFood = false;
		}
		curSq = sq;
		curSq.hasFood = true;
		grid.containsFood = true;
	}
	
}
