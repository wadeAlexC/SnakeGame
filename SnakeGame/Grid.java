package Snakev2;

public class Grid {
	
	//Creates a new array of squares based on the game width and height
	public Square[][] squares;
	boolean containsFood; //if false, will flag Game to create another food object
	public int colorNumber = 0;
	
	public Grid(){
		squares = new Square[(Game.WIDTH / 16)][(Game.HEIGHT / 16)];
		for(int i = 0; i < squares.length; i++){
			for(int j = 0; j < squares[0].length; j++){
				squares[i][j] = new Square(i * 16, j * 16, ObjType.Square, this);
			}
		}
		containsFood = false;
	}
	
}
