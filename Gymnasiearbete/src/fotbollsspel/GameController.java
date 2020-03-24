package fotbollsspel;

import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Image;
import java.lang.String;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

//import se.egy.graphics.TxtContainer;

public class GameController implements MouseListener{
	
	private GameView gv;
	
	private boolean gameRunning = true;
	
	Image backgroundImg = new ImageIcon(getClass().getResource("/bakgrund.png")).getImage();
	private String bgImg = "/bakgrund.png";
	Image ballImg = new ImageIcon(getClass().getResource("/ball.png")).getImage();
	Image goalkeeperImg = new ImageIcon(getClass().getResource("/målvakt.png")).getImage();

	//private boolean Save, Wide, High, Goal = false;
	private boolean Shot = false;
	private boolean checkGoal = false;
	//private boolean multiplayer = false;
	private boolean player1 = true;
	
	private int result;
	
	private int player1Point, player2Point;

	private long lastUpdateTime;
	
	private BallEntity ball;
	
	private BallEntity goalkeeper;
	
	private String amountOfPlayers;
	private int amountOfRounds;
	
	private TextEntity scoreboard;
	//private TxtContainer high, save, wide;

	//private TextEntity goal;
	/**
	 * Bollens start positioner.
	 */
	private double ballPosX;
	private double ballPosY = 400;
	/**
	 * Målvaktens start positioner.
	 */
	private int goalkeeperPosX = 233;
	private int goalkeeperPosY = 160;
	
	private int goalkeeperPos;
	/**
	 * Musens x och y-position.
	 */
	private int mousePosX, mousePosY;
	/**
	 * Bollens hastighet i y och x-led.
	 */
	private double velocityX, velocityY; 
	/**
	 * Tiden det tar för bollen att flytta sg.
	 */
	private double timeForShot = 0.4;
	/**
	 * Variabler för poängstavlan.
	 */
	private int goals, rounds, miss;
	
	private String ScoreboardSingleplayer = "Missar: "+ getMiss() + " - " + "Mål: "+ getGoals() + " " + "Runda: " + getRounds();
	
	private String ScoreboardMultiplayer = "Spelare 1: "+ getPlayer1Point() + " - " + "Spelare 2: "+ getPlayer2Point() + " | Runda " + ((getRounds()/2) + 1);
	
	private String Scoreboard;
	
	//private int width = 640, height = 480;
	
	private Random rand = new Random();
	
	private JFrame parent = new JFrame();
	
	private HashMap<String, Boolean> mouseDown = new HashMap<>();
	
	ArrayList<Entity> spriteList = new ArrayList<>();
	
	 
	
	public GameController(GameView gv) {
		   this.gv = gv;
		
		amountOfPlayers = JOptionPane.showInputDialog("1 eller 2 spelare?");
		amountOfRounds = Integer.parseInt(JOptionPane.showInputDialog("Hur många rundor?"));
		
		if (amountOfPlayers.equals("1")) {
	    	Scoreboard = ScoreboardSingleplayer;
	    }
	    else if (amountOfPlayers.equals("2")) {
	    	Scoreboard = ScoreboardMultiplayer;
	    	//multiplayer = true;
	    }
		
		gv.setMouseListner((MouseListener) this);
		
	    mouseDown.put("clicked", false);
	    loadImages();
	      
	}
	/**
	 * Laddar in spelets bilder och textobjekt.
	 */
	public void loadImages(){
		
	    gv.setBackground(bgImg); 
	    	
	    setBallPosX((gv.getWidth()/2)-ballImg.getWidth(null)/2);
	    
	    goalkeeper = new BallEntity(goalkeeperPosX, goalkeeperPosY, goalkeeperImg,10, 0);
	    
	    ball = new BallEntity(ballPosX, ballPosY, ballImg,getVelocityX(), getVelocityY());
	    
	   /* goal = new TextEntity("Mål!!", 100, 100, null, Color.white);
	    
	    high = new TextEntiy("För högt", 100, 100, null, Color.white);
	    
	    save = new TextEntity("Räddad", 100, 100, null, Color.white);
	    
	    wide = new TextEntity("För brett", 100, 100, null, Color.white); */ 
	   
	    scoreboard = new TextEntity(Scoreboard, 100 , 100, null, Color.white);
	    
	    spriteList.add(goalkeeper);
	    spriteList.add(ball);
	    spriteList.add(scoreboard);
	}
	/**
	 * Loopar igenom programmet när gamerunning är true.
	 */
	public void run(){
		
		lastUpdateTime = System.nanoTime(); 
		
		while(gameRunning) {
			
        	long deltaTime = System.nanoTime() - lastUpdateTime;
        	lastUpdateTime = System.nanoTime();
        	
			update(deltaTime);
			render();
			//renderPoint();
			turnCounter();
			if (amountOfRounds == rounds/2){
				endOfGame();
			}
		}
	}
	/**
	 * Uppdaterar programmet efter varje skott.
	 */
	public void update(long deltaTime) {
		if (Shot) {
				
			ballMovement(deltaTime);
			
			goalkeeperMovement(deltaTime);
			
			if (ball.getYPos() <= (getMousePosY() - (ballImg.getWidth(null)/2))) {
				ball.setSpeed(0, 0);	
			
				//checkGoal = true;
				Shot = false; 
				checkGoal();
				console();
				
				checkGoal = false;
				try {
					Thread.sleep(500);	
				}
				catch (InterruptedException e){}
				
				reset();
			}
			
		}
		if(checkGoal) {
			
			checkGoal();
			console();
			
			checkGoal = false;
			try {
				Thread.sleep(500);	
			}
			catch (InterruptedException e){}
			
			reset();
		}
	}
	/**
	 * Renderar spelets objekt i en Array.
	 */
	public void render() {
		
		gv.beginRender();
	       gv.openRender(spriteList);
	       gv.show();
	}
	/**
	 * Flyttar bollens y-och x-position med hjälp av tiden och musens koordinater.
	 */
	public void ballMovement(long deltaTime) {
	
		setVelocityX((getMousePosX() - (ballPosX+(ballImg.getWidth(null)/2)))/getTime());
		setVelocityY((getMousePosY() - (ballPosY+(ballImg.getHeight(null)/2)))/getTime());
		
		ball.setSpeed(getVelocityX(), getVelocityY());
		
		ball.move(deltaTime);
		
		ball.setSpeed(0, 0);
	}
	/**
	 * Förflyttar målvakten beroende på vilket X 
	 */
	public void goalkeeperMovement(long deltaTime) {
		
		if (getGkPos()  == 401) { 
			goalkeeper.setDirectionX(1);
		}
		else if (getGkPos() == 240) {
			goalkeeper.setDirectionX(0);
		}
		else if (getGkPos() == 79) {
			goalkeeper.setDirectionX(-1);	 
		}
		
		goalkeeper.setSpeed(400, 0);
		goalkeeper.move(deltaTime);
	}
	/**
	 * Kontrollerar om skottet gick i mål är missade.
	 */
	public void checkGoal() {	
		
		if (getMousePosX() >= goalkeeper.getXPos() 
		 && getMousePosX() <= goalkeeper.getXPos() + goalkeeperImg.getWidth(null) 
		 && getMousePosY() >= goalkeeper.getYPos() 
		 && getMousePosY() <= goalkeeper.getYPos() + goalkeeperImg.getHeight(null)) {
			
			//Save = true;
			setMiss(miss + 1);
		}
		else if (getMousePosY() <= 131 ) {
			//High = true;
			setMiss(miss + 1);
		}	
		else if (getMousePosX() <= 70 || getMousePosX() >= 570) {
			//Wide = true;
			setMiss(miss + 1);
		}
		else {
			//Goal = true;
			setGoals(goals + 1);
			if(player1 == true) {
				setPlayer1Point(player1Point + 1);
			}
			else if (player1 == false) {
				setPlayer2Point(player2Point + 1);
			}
		}
		setRounds(rounds +1);
		
		if (amountOfPlayers.equals("1")) {
	    	scoreboard.setText("Missar: "+ getMiss() + " - " + "Mål: "+ getGoals() + " " + " | Runda " + getRounds());
	    	
	    }
	    else if (amountOfPlayers.equals("2")) {
	    	scoreboard.setText("Spelare 1: "+ getPlayer1Point() + " - " + "Spelare 2: "+ getPlayer2Point() + " | Runda " + (getRounds()/2 +1));
	    }	
	}
	/**
	 * Renderar skottets resultat.
	 */
	/*public void renderPoint() {
		if(Save) {
			
			gv.render(save);
			try {
				Thread.sleep(800);
				Save = false;
			}catch (InterruptedException e) {}
		}
		else if(Goal) {
	
			gv.render(goal);
			
			try {		
				Thread.sleep(800);
				Goal = false;
			}catch (InterruptedException e) {}
		}
		else if(Wide) {	
			
			gameScreen.render(wide);
			
			try {	
				Thread.sleep(800);
				Wide = false;
			}catch (InterruptedException e) {}
			
		}
		else if(High) {
			
			gameScreen.render(high);
			
			try {
				Thread.sleep(800);
				High = false;
			}catch (InterruptedException e) {}	
		}
	} */
	/**
	 * Skriver ut resultaten efter varje skott i konsolen.
	 */
	public void console() {						
		System.out.println("Runda: " + getRounds());							
		System.out.println("Missar: "+ getMiss() + " - "+"Mål: " + getGoals());
		System.out.println("Spelare 1 antal poäng: "+ getPlayer1Point());
		System.out.println("Spelare 2 antal poäng: "+ getPlayer2Point());
		System.out.println(getGkPos());
	}
	/**
	 * Startar om spelplanen efter varje skott.
	 */
	public void reset() {						
		goalkeeper.setX(233);
    	goalkeeper.setY(160);
    	ball.setX((gv.getWidth()/2)-ballImg.getWidth(null)/2);
    	ball.setY(400);
    }
	/**
	 * Returnerar vems tur det är.
	 */
	public void turnCounter() {
		if (getRounds()%2 == 0) {
			player1 = true;
		} 
		else {
			player1 = false;
		}	
	}
	/**
	 * Returnerar vinnaren.
	 */
	public void endOfGame() {
		
		if(getPlayer1Point() > getPlayer2Point()){
			result = JOptionPane.showConfirmDialog(parent, "Spelare 1 vann" + "\n" + "Vill du börja ett nytt spel?", null, JOptionPane.YES_NO_OPTION);
		}
		else if(getPlayer1Point() == getPlayer2Point()) {
			result = JOptionPane.showConfirmDialog(parent, "Det blev lika" + "\n" + "Vill du börja ett nytt spel?" , null, JOptionPane.YES_NO_OPTION);
		}
		else {
			result = JOptionPane.showConfirmDialog(parent, "Spelare 2 vann" + "\n" + "Vill du börja ett nytt spel?" , null, JOptionPane.YES_NO_OPTION);	
		}
		if (result == JOptionPane.YES_OPTION) {
			 setPlayer2Point(0);
			 setPlayer1Point(0);
			 setRounds(0); 
			 scoreboard.setText(Scoreboard);
			 return;
		}
		else if (result == JOptionPane.NO_OPTION) {
			System.exit(0);
		}
		else if(result == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
		
	}
	/**
	 * Returner musens position och sätter målvaktes x-position.
	 */
    public void mousePressed(MouseEvent e) {
    	int mouse = e.getClickCount();
		if(mouse == MouseEvent.MOUSE_PRESSED)
	    	 mouseDown.put("clicked", true);
		
		Shot = true;
		
		setGkPos(79 + (rand.nextInt(3) * 161));
		
		setMousePosX(e.getX());
		setMousePosY(e.getY());			
    }
	
    public void setBallPosX(int ballPosX) {
    	this.ballPosX = ballPosX;
    }
    
    public int getMousePosX() {
		return mousePosX;
	}
	public void setMousePosX(int mousePosX) {
		this.mousePosX = mousePosX;
	}

	public int getMousePosY() {
		return mousePosY;
	}
	public void setMousePosY(int mousePosY) {
		this.mousePosY = mousePosY;
	}

	public void mouseReleased(MouseEvent e) {
    }
		
	public int getGoals() {
		return goals;
	}
	public void setGoals(int goals) {
		this.goals = goals;
	}
	
	public int getMiss() {
		return miss;
	}
	public void setMiss(int miss) {
		this.miss = miss;
	}
	
	public int getRounds() {
		return rounds;
	}
	public void setRounds(int rounds) {
		this.rounds = rounds;
	}
	
	public void setScoreboardSingleplayer(String scoreboardSingleplayer) {
		this.ScoreboardSingleplayer = scoreboardSingleplayer;
	}
	
	public double getVelocityX() {
		return velocityX;
	}
	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}
	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public double getTime() {
		return timeForShot;
	}
	
	public void mouseClicked(MouseEvent e) {		
	}

	public void mouseEntered(MouseEvent e) {	
	}

	public void mouseExited(MouseEvent e) {	
	}

	public int getGkPos() {
		return goalkeeperPos;
	}

	public void setGkPos(int gkPos) {
		this.goalkeeperPos = gkPos;
	}
	
	public int getPlayer1Point() {
		return player1Point;
	}

	public void setPlayer1Point(int player1Point) {
		this.player1Point = player1Point;
	}

	public int getPlayer2Point() {
		return player2Point;
	}

	public void setPlayer2Point(int player2Point) {
		this.player2Point = player2Point;
	}
	
	public int getAmountOfRounds() {
		return amountOfRounds;
	}

	public void setAmountOfRounds(int amountOfRounds) {
		this.amountOfRounds = amountOfRounds;
	}

	public String getScoreboard() {
		return Scoreboard;
	}
	public void setScoreboard(String scoreboard) {
		Scoreboard = scoreboard;
	}
	
	
}