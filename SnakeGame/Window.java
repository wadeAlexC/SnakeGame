import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas{
	
	//
	private static final long serialVersionUID = 1L;
	//
	
	public Window(int width, int height, String name, Game game){
		JFrame frame = new JFrame(name);
		Container c = frame.getContentPane();
		c.setPreferredSize(new Dimension(width,height));
		//frame.setPreferredSize(new Dimension(width,height + 10));
		//frame.setMaximumSize(new Dimension(width,height + 10));
		//frame.setMinimumSize(new Dimension(width,height + 10));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//change when game is complete to false
		//not changing- game displays differently on macs
		frame.setResizable(true);
		frame.add(game);
		frame.pack();
		frame.setVisible(true);
		frame.requestFocus();
		game.start();
	}
	
}
