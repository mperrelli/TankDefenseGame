/*
 * Tank Defense
 * By Matthew Perrelli
 */

import java.awt.Frame;

public class main {
	public static void main(String[] args) {
		
		TankWorld w = new TankWorld();
		TankScreen s = new TankScreen(w);
		
		//Build the main window for the game to run in
		Frame f = new Frame("Tank Defense");
		f.addWindowListener(new Closer());
		f.add(s);
		f.pack();
		f.setVisible(true);
		
		//Control listeners
		s.addKeyListener(new TankController(w));
		s.addMouseListener(new MouseController(w));
		
		//Write the titlescreen while the program waits for the 'Enter' button
		//to be clicked.
		while(w.titlescreen == true){
			s.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Main game loop 
		while (!w.gameOver()) {
			w.advanceTime();
			s.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Shows high scores after the game loop exits
		w.reorderhs();
		w.showhs = true;
		while (w.showhs == true){
			s.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
