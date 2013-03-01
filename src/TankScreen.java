import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class TankScreen extends Canvas {
	private TankWorld world;
	private int width,height;
	final private int T = 30;
	int bopacity = 0;
	//Declares all the images to be drawn
    Image backgroundi = null;
    Image etank = null;
    Image amissile = null;
    Image sandbag = null;
    Image panelbg = null;
    Image p_missile = null;
    Image mytank = null;
    Image button_unav = null;
    Image button_avail = null;
    Image button_used = null;
    Image titlescreen = null;
    Image titlescreen_hs = null;
    Image lifemeterbg = null;
    Image lifemeterglass = null;
    
    //The absolute path to my images folder on my computer.
    String abspath = "/Users/Matt/School/Sophomore/Java/TankDefense/src/images/";
    
    //Predefined fonts
    Font font1 = new Font("Helvetica", Font.BOLD,  16);
    Font font2 = new Font("Helvetica", Font.BOLD,  20);
    Font font3 = new Font("Helvetica", Font.ITALIC + Font.BOLD, 20);
    Font font4 = new Font("Helvetica", Font.BOLD, 15);
	
	public TankScreen(TankWorld w){
		world = w;
		height = world.height();
		width = world.width();
		setSize(T * world.width(), T * world.height());
		//Images loaded from file
		backgroundi = Toolkit.getDefaultToolkit().getImage(abspath + "dirtbg.jpg");
		amissile = Toolkit.getDefaultToolkit().getImage(abspath + "missile.png");
		etank = Toolkit.getDefaultToolkit().getImage(abspath + "tank.png");
		sandbag = Toolkit.getDefaultToolkit().getImage(abspath + "sandbag.png");
		panelbg = Toolkit.getDefaultToolkit().getImage(abspath + "panelbg.png");
		p_missile = Toolkit.getDefaultToolkit().getImage(abspath + "p_missile.png");
		mytank = Toolkit.getDefaultToolkit().getImage(abspath + "mytank.png");
		button_unav = Toolkit.getDefaultToolkit().getImage(abspath + "buttonunavailable.png");
		button_avail = Toolkit.getDefaultToolkit().getImage(abspath + "buttonavail.png");
		button_used = Toolkit.getDefaultToolkit().getImage(abspath + "buttonused.png");
		titlescreen = Toolkit.getDefaultToolkit().getImage(abspath + "titlescrn.png");
		titlescreen_hs = Toolkit.getDefaultToolkit().getImage(abspath + "titlescrn_hs.png");
		lifemeterbg = Toolkit.getDefaultToolkit().getImage(abspath + "lifemeterbg.png");
		lifemeterglass = Toolkit.getDefaultToolkit().getImage(abspath + "lifemeterglass.png");
	}
	
	@SuppressWarnings("unchecked")
	public void paint(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(backgroundi, 0, 0, null);
		g.setColor(Color.BLACK);
		//Draws the base board set up in the world.
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){
				Character tile = world.get(x,y);
				switch (tile) {
					case '#':
						g.drawImage(sandbag, x*T, y*T, null);
						break;
					case '.':
						break;
					case 'b':
						g.setColor(Color.BLUE);
						g.drawImage(panelbg, x*T, y*T - (T/2), null);
						g.setColor(Color.BLACK);
						break;
					case 'O':
						g.drawOval(x*T, y*T, T, T);
						break;
					default:
						System.out.println("Whats is that?");
				}
			}
		}
		
		//Draw ammo in the Bottom Panel
		if (world.ammo == 0){
			if (world.time % 5 == 0 || world.time % 5 == 1){
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.BLACK);
			}
			
			g.setFont(font1);
			g.drawString("You need more ammo!", T*20, T*11 + (T/2));
		}
		else{
			for (int i = 1; i <= world.ammo; i++){
				g.drawImage(p_missile, (i * -T/(T/10)) + (T * 30), (T/(T/10) * T) + T/(T/10) + T/3 - 2, null);
			}
		}
		
		//draw score, currency, and kill count
		g.setColor(Color.BLACK);
		g.setFont(font2);
		g.drawString("Score", 2, T*11 + 5);
		g.drawString("Currency", T*2 + 5, T*11 + 5);
		g.drawString("Kills", T*5 + 5, T*11 + 5);
		g.setFont(font3);
		g.drawString(Integer.toString(world.score), 2, T*11 + T/2 + 8);
		g.drawString(Integer.toString(world.currency), T*2 + 5, T*11 + T/2 + 8);
		g.drawString(Integer.toString(world.killcount), T*5 + 5, T*11 + T/2 + 8);
		
		
		//draw missiles
		for (Missile missile : (ArrayList<Missile>)world.missiles.clone()){
			g.drawImage(amissile, (int)(missile.x*T) + T/3, (int)(missile.y*T) + T/3, null);
		}
		
		//draw tank
		g.drawImage(mytank, (int)(world.tankx*T), (int)(world.tanky*T), null);
		
		// Draws enemy tanks
		for (Optank optank : (ArrayList<Optank>)world.optanks.clone()){
			g.drawImage(etank, (int)(optank.x*T) - T/2, (int)(optank.y*T) - (T - (T/4)), null);
			
		}
		
		//Draw upgrade buttons
		//Draw Ammo upgrade button
		g.setFont(font4);
		if (world.currency >= 1000 && world.ammo_used == false){
			g.setColor(Color.BLACK);
			g.drawImage(button_avail, T*7, T*10 + T/2 + 2, null);
			g.drawString("+Ammo", T*7 + 5, T*11 + T/2 - 2);
		}
		else if (world.ammo_used == true){
			g.setColor(Color.RED);
			g.drawImage(button_used, T*7, T*10 + T/2 + 2, null);
			g.drawString("+Ammo", T*7 + 5, T*11 + T/2 - 2);
		}
		else{
			g.setColor(Color.GRAY);
			g.drawImage(button_unav, T*7, T*10 + T/2 + 2, null);
			g.drawString("+Ammo", T*7 + 5, T*11 + T/2 - 2);
		}
		
		//Draw missile upgrade button
		if (world.currency >= 3000 && world.strongbullets_used == false){
			g.setColor(Color.BLACK);
			g.drawImage(button_avail, T*9 + T/2, T*10 + T/2 + 2, null);
			g.drawString("Strong", T*9 + T/2 + 9, T*11 + 3);
			g.drawString("Bullets", T*9 + T/2 + 9, T*11 + 20);
		}
		else if (world.strongbullets_used == true){
			g.setColor(Color.RED);
			g.drawImage(button_used, T*9 + T/2, T*10 + T/2 + 2, null);
			g.drawString("Strong", T*9 + T/2 + 9, T*11 + 3);
			g.drawString("Bullets", T*9 + T/2 + 9, T*11 + 20);
		}
		else{
			g.setColor(Color.GRAY);
			g.drawImage(button_unav, T*9 + T/2, T*10 + T/2 + 2, null);
			g.drawString("Strong", T*9 + T/2 + 9, T*11 + 3);
			g.drawString("Bullets", T*9 + T/2 + 9, T*11 + 20);
		}
		
		//Draw airstrike upgrade button
		if (world.currency >= 7000){
			g.setColor(Color.BLACK);
			g.drawImage(button_avail, T*12, T*10 + T/2 + 2, null);
			g.drawString("Airstrike", T*12 + 5, T*11 + T/2 - 2);
		}
		else{
			g.setColor(Color.GRAY);
			g.drawImage(button_unav, T*12, T*10 + T/2 + 2, null);
			g.drawString("Airstrike", T*12 + 5, T*11 + T/2 - 2);
		}
		
		
		
		//draw life meter
		if (world.cfill >= 270){
			g.setColor(Color.GREEN);
		}
		else if (world.cfill > 180){
			g.setColor(Color.YELLOW);
		}
		else if(world.cfill > 90){
			g.setColor(Color.ORANGE);
		}
		else if(world.cfill > 0){
			g.setColor(Color.RED);
		}
		g.drawImage(lifemeterbg, T*15 - 15, T*10 + T/2, null);
		g.fillArc(T*15 - 8, T*10 + T/2 + 3, 40, 35, 0, world.cfill);
		g.drawImage(lifemeterglass, T*15 - 15, T*10 + T/2, null);
		
		//Draw title screen
		if (world.titlescreen == true){
			g.drawImage(titlescreen, 0, 0, null);
		}
		
		//Draw highscore screen and write the high score list
		if (world.showhs == true){
			g.drawImage(titlescreen_hs, 0, 0, null);
			g.setColor(Color.BLACK);
			int i = 1;
			for (String hs : world.highscores){
				if (i < 11){
					String dline = i + ".  " + hs + "%";
					g.drawString(dline, 510, 105 + 16*(i));
				}
				i++;
			}
			
		}
		
		
	}
	
}
