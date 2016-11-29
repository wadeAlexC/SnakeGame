package Snakev2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Square extends GameObject{
	
	//Booleans represent whether a square has Food or a SnakePiece on it
	public boolean hasSnake;
	public boolean hasFood;
	public Color[] colors = new Color[6];
	Random r;
	public Grid grid;
	
	
	//Constructor method for Square object
	public Square(int x, int y, ObjType type, Grid grid) {
		super(x, y, type);
		colors[0] = Color.CYAN;
		colors[1] = Color.GREEN;
		colors[2] = Color.MAGENTA;
		colors[3] = Color.ORANGE;
		colors[4] = Color.PINK;
		colors[5] = Color.WHITE;
		r = new Random();
		this.grid = grid;
	}

	public void tick() {
		//has no tick method- squares will update when SnakePiece moves onto them (curSq field in SnakePiece obj updates Square bools)
	}
	
	//Render method for each square- 
	//Color depends on whether the square has food, snake, or nothing on it.
	public void render(Graphics g) {
		if(hasSnake){
			Color c = colors[grid.colorNumber];
			g.setColor(c); //TODO make random worm color
			g.fillRect(x, y, 16, 16);
			g.setColor(Color.BLACK);
			g.drawRect(x, y, 16, 16);
			return;
		}
		
		if(hasFood){
			g.setColor(Color.BLUE); //food color
			g.fillRect(x, y, 16, 16);
			g.setColor(Color.BLACK);
			g.drawRect(x, y, 16, 16);
			return;
		}
		
		//else, there is no food or worm here so set color to dark gray
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y, 16, 16);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, 16, 16);
	}
	
	//toString method for Square displays X and Y coordinates
	public String toString(){
		return "X: " + this.x + ", Y: " + this.y;
	}
	
}
