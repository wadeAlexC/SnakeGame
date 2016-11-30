import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
	
	private Handler handler;
	
	public KeyInput(Handler handler){
		this.handler = handler;
	}
	
	//Determines what happens when a key is pressed
	public void keyPressed(KeyEvent k){
		//get key code for the key pressed
		int key = k.getKeyCode();
		System.out.println(k);
		
		//if the game is over
		if(handler.gameOver){
			
			if(key == KeyEvent.VK_1){
				System.out.println("Playing again: ");
				handler.setPromptResponse(1);
			} else if (key == KeyEvent.VK_2){
				System.out.println("Closing...");
				handler.setPromptResponse(2);
			}
		}
		
		//iterates over each gameobject to find the worm, assigning it to a temp value
		for(GameObject obj : handler.list){
			GameObject temp = obj;
			
			if(temp.type == ObjType.Snake){
				SnakePiece s = (SnakePiece)temp;
				
				//Pauses/unpauses the game when 'p' is pressed by suspending the tick method in handler
				if(key == KeyEvent.VK_P) handler.pauseGame();
				
				//raises the difficulty (increases the snake speed) when '.' is pressed
				if(key == KeyEvent.VK_PERIOD){ handler.raiseDifficulty();
				System.out.println("RAISED");}
				//lowers the difficulty (decreases the snake speed) when ',' is pressed
				if(key == KeyEvent.VK_COMMA) handler.lowerDifficulty();
				
				if(s.getSize() <= 1){ //if the Snake's size is 1, it can move in any direction
					
					/*
					 * Snake control uses standard WASD keys for movement control
					 */
					if(key == KeyEvent.VK_S){ //down
						moveDown(s);
					} else if(key == KeyEvent.VK_W){ //up
						moveUp(s);
					} else if(key == KeyEvent.VK_A){ //left
						moveLeft(s);
					}else if(key == KeyEvent.VK_D){ //right
						moveRight(s);
					}
					/*
					 * 
					 */
					
				} else { //Snake's size is larger than 1, meaning it cannot move backward (determined by velocity)
					
					if(key == KeyEvent.VK_S && s.velY != -1){ //down
						
						if(s.curSq.x == s.getChild().curSq.x){ //you shouldn't be able to move down
							break;						   //if the worm is in the same x plane as its child
						}
						moveDown(s);
						
					} else if(key == KeyEvent.VK_W && s.velY != 1){ //up
						
						if(s.curSq.x == s.getChild().curSq.x){
							break;
						}
						moveUp(s);
						
					} else if(key == KeyEvent.VK_A && s.velX != 1){ //left
						
						if(s.curSq.y == s.getChild().curSq.y){
							break;
						}
						moveLeft(s);
						
					}else if(key == KeyEvent.VK_D && s.velX != -1){ //right
						
						if(s.curSq.y == s.getChild().curSq.y){
							break;
						}
						moveRight(s);
						
					}
				}
			}
		}
	}
	
	public void moveUp(SnakePiece s){
		s.velX = 0;
		s.velY = -1;
	}
	
	public void moveDown(SnakePiece s){
		s.velX = 0;
		s.velY = 1;
	}
	
	public void moveLeft(SnakePiece s){
		s.velX = -1;
		s.velY = 0;
	}
	
	public void moveRight(SnakePiece s){
		s.velX = 1;
		s.velY = 0;
	}
	
}
