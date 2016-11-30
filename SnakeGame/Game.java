import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.Random;

public class Game extends Canvas implements Runnable{
	
	//
	private static final long serialVersionUID = 1L;
	//
	
	//Sizes for window
	public static final int WIDTH = 640, HEIGHT = (int)(WIDTH / 12.0 * 9.0); //size converted to double to avoid rounding and graphics errors
	
	private boolean running = false;
	//
	private Handler handler;
	
	private Thread thread;
	
	Random r;
	
	public int colorNum = 0;
	
	public SnakePiece gameSnake; //head of game snake
	
	public enum State{
		Menu,
		Game,
		Prompt,
	};
	
	State gameState = State.Game;
	
	/*
	 * End Class Variables
	 */
	
	//Const. method for Game object
	public Game(){
		r = new Random();
		
		handler = new Handler();
		
		this.addKeyListener(new KeyInput(handler));
		
		
		//Creates the grid and squares and adds them to the handler
		//Each square represents 16X16 pixel block
		Grid grid = new Grid();
		handler.grid = grid;
		for(int i = 0; i < grid.squares.length; i++){
			for(int j = 0; j < grid.squares[0].length; j++){
				handler.addObject(grid.squares[i][j]);
			}
		}
		
		System.out.println(Arrays.deepToString(grid.squares));
		
		gameSnake = new SnakePiece(80, 32, ObjType.Snake, grid);		
		
		if(gameState == State.Game){
			handler.addObject(gameSnake); //adds the Snake to the handler
			
		}
		
		//create a window for everything to be displayed on
		new Window(WIDTH, HEIGHT, "Snake Remake", this);
		this.requestFocusInWindow(); //makes sure the game works without having to click the window
	}
	
	/*
	 * End Constructor Method
	 */
	
	//Run method for Runnable interface
	//Contains a simple game loop that calls tick() and render() if running == true
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = handler.getDifficulty();
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running){
			ns = 1000000000 / handler.getDifficulty(); //updates the difficulty
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				delta--;
			}

			if(running) render();
			
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	//tick method- describes a single tick of the game
	private void tick(){
		
		if(gameState == State.Game){
			
			if(handler.list.size() == 0){ //Should only happen when the game is over
				gameState = State.Prompt; //Prompts the user to enter a command
			}
			
			//tick handler
			if(!handler.gameOver) {
				handler.tick();
			} else {
				gameState = State.Prompt;
			}
			//
			

		} else if (gameState == State.Prompt){
			System.out.println("PROMPT");
			int prompt = handler.getPromptResponse();
			if(prompt == 1){ //play again
				//Clear handler list
				handler.clearList();
				
				/*
				 * Add objects back to handler
				 */
				Grid grid = new Grid();
				handler.grid = grid;
				for(int i = 0; i < grid.squares.length; i++){
					for(int j = 0; j < grid.squares[0].length; j++){
						handler.addObject(grid.squares[i][j]);
					}
				}
				
				gameSnake = new SnakePiece(80, 32, ObjType.Snake, grid);
				handler.addObject(gameSnake);
				/*
				 * 
				 */
				
				//revert handler booleans to original settings
				handler.gameOver = false;
				handler.setPromptResponse(0);
				//set gameState back to State.Game
				gameState = State.Game;
				
			} else if (prompt == 2){
				System.exit(1);
			}
		}
		
	}

	//render method
	private void render(){
		if(colorNum >= 36){
			colorNum = 0;
		}
		BufferStrategy bs = this.getBufferStrategy(); 
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		if(gameState == State.Game){
			handler.render(g);
		} else if (gameState == State.Prompt){ //draws a basic prompt screen for the user
			g.setColor(Color.WHITE);
			Font promptFont = new Font("Serif", Font.BOLD, 32);
			g.setFont(promptFont);
			g.drawString("Game Over! To Play Again, Press 1", 50, 50);
			g.drawString("To close the window, press 2" , 50, 100);
		}
		
		
		g.dispose();
		bs.show();
	}
	
	//Synchronized method starts thread (called in Window constructor)
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	//Synchronized method stops thread (called when game is over)
	public synchronized void stop(){
		try{
			thread.join();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
