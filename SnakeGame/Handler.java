import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

public class Handler {
	
	//list contains every game object- the tiles of the game, the snake, and the food objects
	LinkedList<GameObject> list = new LinkedList<GameObject>();
	Grid grid;
	Random r = new Random();
	
	private boolean isGamePaused = false;
	boolean gameOver;
	
	private int promptResponse = 0;
	
	private int difficulty = 20;
	
	//tick method for handler
	public void tick(){
		if(isGamePaused == true){ //does not use the tick method if the game is paused
			return;
		}
		Food f = null;
		for(GameObject obj : list){
			
			//If the object is a SnakePiece and there is no food on the grid,
			//we need the coordinates of its head in order to place a new food object down
			if(obj.type == ObjType.Snake && !grid.containsFood){
				SnakePiece temp = (SnakePiece)obj;
				//assess gameOver
				if(temp.isGameOver()){
					gameOver = true;
					return;
				}
				
				int snakeX = 0;
				int snakeY = 0;
				if(temp.getParent() == null){
					snakeX = temp.curSq.x;
					snakeY = temp.curSq.y;
					
					//Find a random set of coordinates for the next piece of food
					int randFX = r.nextInt(Game.WIDTH / 16);
					randFX *= 16;
					int randFY = r.nextInt(Game.HEIGHT / 16);
					randFY *= 16;
					
					//If the coordinates are on the same spot as the worm, we need new coordinates
					while((randFX == snakeX && randFY == snakeY) || (randFX >= 624 || randFY >= 448)){
						randFX = r.nextInt(Game.WIDTH / 16);
						randFX *= 16;
						randFY = r.nextInt(Game.HEIGHT / 16);
						randFY *= 16;
					}
					
					//create a new food object
					f = new Food(randFX, randFY, ObjType.Food);
					//because adding it to the list immediately produces a concurrent modification exception,
					//the object is added upon termination of the loop
					
					//give it a square
					f.curSq = grid.squares[f.x / 16][f.y / 16];
					f.curSq.hasFood = true;
					
				} else { //Children aren't added to handler.list, so this should never happen
					System.out.println("Error- children in list");
					break;
				}
			}
			
			/*
			 * Detecting whether the Snake has hit food or itself
			 */
			if(obj.type == ObjType.Snake){
				SnakePiece temp = (SnakePiece)obj; 
				//assess gameOver
				if(temp.isGameOver()){
					gameOver = true;
					return;
				}
				
				//Food
				if(temp.curSq.hasFood){
					temp.eatFood();
					temp.curSq.hasFood = false;
					grid.containsFood = false;
				}
				//
				
				if(temp.getChild() != null){
					SnakePiece temp2 = temp.getChild();
					while(temp2 != null){
						/*
						 * gameover condition- if the Snake head hits one of its pieces, game over
						 */
						if(temp2.curSq.x == temp.curSq.x && temp2.curSq.y == temp.curSq.y){
							System.out.println("GAME OVER");
							gameOver = true; //assessed in Game.tick()
							return;
						}
						
						temp2 = temp2.getChild();
					}
				}
			}
			obj.tick();
		}
		//If f is not null, a food object was created and it should be added to the handler
		//The grid then contains food, so the corresponding boolean is set to true
		if(f != null){
			addObject(f);
			grid.containsFood = true;
		}
	}
	
	//render method for handler iterates over each GameObject and renders them
	public void render(Graphics g){
		for(GameObject obj : list){
			obj.render(g);
		}
	}
	
	//Pause the game by suspending the tick method
	public void pauseGame(){
		isGamePaused = !isGamePaused;
	}
	
	//Raises the game's difficulty (increases snake speed)
	public void raiseDifficulty(){
		System.out.println("OLD DIFF: " + difficulty);
		if(difficulty == 15){
			difficulty = 20;
		} else if (difficulty == 20){
			difficulty = 30;
		} else {
			difficulty = 45;
		}
		System.out.println("NEW DIFF: " + difficulty);
	}
	
	//lowers the game's difficulty (decreases snake speed)
	public void lowerDifficulty(){
		if(difficulty == 45){
			difficulty = 30;
		} else if(difficulty == 30){
			difficulty = 20;
		} else {
			difficulty = 15;
		}
	}
	
	//returns the difficulty level
	public int getDifficulty(){
		return difficulty;
	}
	
	public void setPromptResponse(int response){
		promptResponse = response;
	}
	
	public int getPromptResponse(){
		return promptResponse;
	}
	
	public void clearList(){
		list.clear();
	}
	
	public void addObject(GameObject obj){
		list.add(obj);
	}
	
	public void remObject(GameObject obj){
		list.remove(obj);
	}
}
