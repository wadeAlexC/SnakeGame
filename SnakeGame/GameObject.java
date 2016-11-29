package Snakev2;

import java.awt.Graphics;

public abstract class GameObject {
	
	//Required fields for any gameObject
	protected int x,y;
	protected ObjType type;
	protected int velX, velY;
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	
	//GameObject Constructor
	public GameObject(int x, int y, ObjType type){
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	
	/*
	 * Set/Get methods below
	 */
	
	
	public void setX(int x){
		this.x = x;
	}
		
	public void setY(int y){
		this.y = y;
	}
		
	public int getX(){
		return x;
	}
		
	public int getY(){
		return y;
	}
		
	public void setType(ObjType type){
		this.type = type;
	}
	
	public ObjType getType(){
		return type;
	}
		
	public void setVelX(int velX){
		this.velX = velX;
	}
		
	public void setVelY(int velY){
		this.velY = velY;
	}
		
	public int getVelX(){
		return velX;
	}
		
	public int getVelY(){
		return velY;
	}	
}
