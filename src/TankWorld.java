import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TankWorld {
	public final static int STILL = 0, UP = 1, DOWN = 2; //Directional constants
	float tankx = 27; //Tank Starting position-x
	float tanky = 3; //Tank Starting position-y
	int scorescount = 0; //Variable used to store the amount of high scores loaded from file.
	public int ammo = 30, ammocap = 30; //The current ammo and the limit
	int tankdir = STILL; //Current tank direction based on directional constants
	public int nexttankdir = STILL; //Next tank direction
	int dlow = 1; //Current low difficulty rating '1-3'
	int dhigh = 1; //Current high difficulty rating '1-3'
	int SpawnFrequency = 80; //The frequency by which tanks will spawn
	int DifficultyInterval = 200; //How quickly the difficulty advances
	//Boolean values that keep track of the current state of the game
	boolean reloading = false, startshooting = false, titlescreen = true, showhs = false;
	public int killcount = 0, currency = 0, lives = 5, score = 0; //Current game data
	int hitpoint = 1; //How strong each bullet hits for
	boolean ammo_used = false, strongbullets_used = false; //Keeps track of used upgrades
	int fillp = (360 / lives); //Calculates how lives effect the life meter
	int cfill = 360; //Current life meter fill
	public float time = 0; //Time variable
	int missilehit = 0, missilemiss = 0; float hitpercent; //Keeps track of hits and misses
	
	//The absolute path to my program
	String datafile = "/Users/Matt/School/Sophomore/Java/TankDefense/src/scores.txt";
	
	/*Declares ArrayLists to be used
	 * Missile - holds the necessary data for all the missiles on the screen.
	 * optanks - holds the position information for the enemy tanks.
	 * highscores - holds the highscores loaded from the datafile.
	 * 
	 * The basis for the board is drawn as a list of string arrays.
	 */
	ArrayList<Missile> missiles = new ArrayList<Missile>(); 
	ArrayList<Optank> optanks = new ArrayList<Optank>();
	ArrayList<String> highscores = new ArrayList<String>();
	private String board =  "##############################\n"
			              + "..............................\n" 
			              + "..............................\n"
			              + "..............................\n" 
			              + "..............................\n" 
			              + "..............................\n"
			              + "..............................\n" 
			              + "..............................\n" 
			              + "..............................\n"
			              + "..............................\n"
			              + "##############################\n"
			              + "b............b............b...\n";
	
	private ArrayList<ArrayList<Character>> boarda = new ArrayList<ArrayList<Character>>();
	
	/*
	 * Constructs the world based on out string array
	 */
	public TankWorld(){
		String[] rows = board.split("\n");
		for (String row : rows) {
			ArrayList<Character> newrow = new ArrayList<Character>();
			for(int i = 0; i < row.length(); i++){
				newrow.add(row.charAt(i));
			}
			boarda.add(newrow);
		}
		
		readhighscores(); //Reads high scores for later use in the program
		
	}
	
	
	private void readhighscores() {
		FileReader in = null;
		String line = null;
		try {
			in = new FileReader(datafile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(in);
		try {
			line = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		processLine(line);
		while(line != null){
			try {
				line = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
				if (line != null){
					processLine(line);
				}
			}
		
	}

	private void processLine(String line) {
		highscores.add(line);
	}
	
	private String buildrecord(int score, int kills, float accuracy){
		return score + "                " + kills + "                " + accuracy;
		
	}
	
	//Function to ouput a random integer based on a range of input.
	public int rand(int min, int max){
		return min + (int)(Math.random() * max);
	}
	
	//Returns height of the board
	public int height(){
		return boarda.size();
	}
	
	//Returns width of the board
	public int width(){
		return boarda.get(0).size();
	}
	
	//Gets a character on the board at a givin location
	public Character get(int x, int y) {
		return boarda.get(y).get(x);
	}
	
	/*
	 * gameOver controls the main game loop.
	 * When lives reaches 0 gameover is set to true and
	 * the score is written to the highscore array and written to the datafile
	 */
	public boolean gameOver() {
		if (lives == 0){
			float temp = missilehit + missilemiss;
			hitpercent = (missilehit / temp) * 100;
			processLine(buildrecord(score,killcount,(int)hitpercent));
			writetofile();
			return true;
		}
		else{
			return false;
		}
	}

	private void writetofile() {
		FileWriter out = null;
		try {
			out = new FileWriter(datafile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(out);
		try {
			for(String a : highscores){	
				if (a != null){
					bw.write(a + "\n");
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * advanceTime holds all the key information for updating the state of the game.
	 * Keeps track of advancing...
	 * 1. The tanks positions and speeds
	 * 2. The state of the GUI bar
	 * 3. Current movement of the users tank based on controller input
	 * 4. How fast difficulty increases
	 * 5. currency values
	 */
	
	public void advanceTime() {
		time += 1;
		
		int x = Math.round(tankx);
		int y = Math.round(tanky);
		
		//Checks if the user can continue to reload
		if (time % 8 == 0 && reloading == true && currency > 9){
			ammo += 1;
			currency -= 10;
			
		}
		if (ammo == ammocap){
			reloading = false;
		}
		
		//Checks to see of the users tank can continue to move in a specified direction.
		if (Math.abs(x - tankx) < .1 && Math.abs(y - tanky) < .1){
			tankdir = nexttankdir;
			if (tankdir == UP && get(x, y-1) == '#'){
				tankdir = STILL;
			}
			if (tankdir == DOWN && get(x, y+1) == '#'){
				tankdir = STILL;
			}
			
		}
		
		//Advances the position of the tank based on input
		if (tankdir == STILL){
			tanky += 0;
		}
		if (tankdir == UP){
			tanky -= .2;
		}
		if (tankdir == DOWN){
			tanky += .2;
		}
		
		//Makes the game more difficult over time
		if (Math.round(time) % DifficultyInterval == 0){
			if (SpawnFrequency != 20){
				SpawnFrequency -= 3;
			}
		}
		if (SpawnFrequency < 25){
			dhigh = 3;
			dlow = 2;
		}
		else if (SpawnFrequency < 40){
			dhigh = 3;
		}
		else if (SpawnFrequency < 65){
			dlow = 2;
		}
		
		//Adds another tank to the arraylist of optanks
		if (Math.round(time) % SpawnFrequency == 0){
			optanks.add(new Optank(-2, rand(1,9), rand(dlow,dhigh)));
		}
		
		//Checks the state of all optanks on the screen
		for (int i = 0; i < optanks.size(); i++){
			Optank optank = optanks.get(i);
			
			if(optank.x > 30 + (1/2)){
				optanks.remove(i);
				lives -= 1;
				cfill -= fillp;
			}
			
			if (optank.level == 1){
				optank.x += .02 + (Math.random()*.05);
			}
			if (optank.level == 2){
				optank.x += .05 + (Math.random()*.1);
			}
			if (optank.level == 3){
				optank.x += .1 + (Math.random()*.15);
			}
			if (optank.level <= 0){
				optanks.remove(i);
				killcount += 1;
				score += 9;
				if (optank.startinglevel == 1){
					currency += 100;
				}
				else if (optank.startinglevel == 2){
					currency += 120;
				}
				else if (optank.startinglevel == 3){
					currency += 150;
				}
				break;
			}
		}
		
		//Checks the state of all missiles on the screen and their collisions
		for (int i = 0; i < missiles.size(); i++){
			boolean remove = false;
			Missile missile = missiles.get(i);
			for (Optank optank : optanks){
				if ((missile.x < optank.x + .5 && missile.x > optank.x - .5) &&
						(missile.y < optank.y + .5 && missile.y > optank.y - .5)){
					optank.level -= hitpoint;
					remove = true;
					missilehit += 1;
					break;
				}
			}
			missile.x -= .5;
			if (missile.x < -5){
				remove = true;
				missilemiss += 1;
			}
			
			if(remove == true){
				missiles.remove(i);
			}
		}
		
	}
	
	//Clears the board
	public void airstrike() {
		int count = 0;
		for (@SuppressWarnings("unused") Optank optank : optanks){
			count += 1;
		}
		score = score + (count * 9);
		optanks.clear();
		
	}

	//Puts high scores in the correct order
	public void reorderhs() {
		int[] scores = new int[highscores.size()];
		for(int i = 0; i < highscores.size(); i++)
			scores[i] = Integer.parseInt(highscores.get(i).split(" ")[0]);
		Arrays.sort(scores);
		ArrayList<String> temp = new ArrayList<String>();
		for(int i = scores.length - 1; i >= 0; i--)
			for(int j = 0; j < highscores.size(); j++)
				if(scores[i] == Integer.parseInt(highscores.get(j).split(" ")[0]))
				{
					temp.add(highscores.get(j));
				}
		highscores = temp;
	}			
}
