package controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Entity;
import model.ObjectEntity;
import model.TextEntity;
import view.GameView;

/**
 * @author Pontus Gustafsson
 */
public class GameController implements MouseListener{

	private GameView gv;

	private boolean gameRunning = true;

	Image backgroundImg = new ImageIcon(getClass().getResource("/bakgrund.png")).getImage();
	Image ballImg = new ImageIcon(getClass().getResource("/ball.png")).getImage();
	Image goalkeeperImg = new ImageIcon(getClass().getResource("/målvakt.png")).getImage();
	
	private String bgImg = "/bakgrund.png";

	private boolean Shot = false;
	
	private boolean multiplayer = false;
	private boolean singleplayer = false;
	
	private boolean player1 = true;

	private int endResult;

	private int player1Point, player2Point;

	private ObjectEntity ball;
	private ObjectEntity goalkeeper;
	
	private TextEntity Scoreboard;
	private TextEntity announcer;

	private int nrOfPlayers;
	private int nrOfRounds;

	private double ballPosX, ballPosY;
	
	private int goalkeeperPosX, goalkeeperPosY;

	private int goalkeeperPos;

	private int mousePosX, mousePosY;	

	private double velocityX, velocityY; 	

	private double timeForShot = 0.4;
	
	private int goals, rounds, miss;

	private String ScoreboardSingleplayer = "Missar: "+ getMiss() + " - " + "Mål: "+ getGoals() + " " + "Runda: " + getRounds();

	private String ScoreboardMultiplayer = "Spelare 1: "+ getPlayer1Point() + " - " + "Spelare 2: "+ getPlayer2Point() + " | Runda " + ((getRounds()/2) + 1);

	private String scoreboard = "";
	
	private Random rand = new Random();

	private JFrame parent = new JFrame();

	private HashMap<String, Boolean> mouseDown = new HashMap<>();

	private ArrayList<Entity> spriteList = new ArrayList<>();

	/**
	 * Spelets konstruktor.
	 * @param GameView gv
	 */
	public GameController(GameView gv) {
		this.gv = gv;

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
		
		setBallPosY((gv.getHeight()/6) *5);
		
		setGoalkeeperPosX(gv.getWidth()/4);
		
		setGoalkeeperPosY((int)(gv.getHeight()/2.06));
		
		goalkeeper = new ObjectEntity(goalkeeperPosX, goalkeeperPosY, goalkeeperImg,10, 0);

		ball = new ObjectEntity(ballPosY, ballPosX, ballImg,getVelocityX(), getVelocityY());
		
		announcer = new TextEntity(" ", 400, 100, null, Color.white);

		Scoreboard = new TextEntity(scoreboard, 100 , 100, null, Color.white);

		spriteList.add(goalkeeper);
		spriteList.add(ball);
		spriteList.add(Scoreboard);
		spriteList.add(announcer);
	}
	
	/**
	 * Loopar igenom programmet när gamerunning är true.
	 */
	public void run(){
		int fps = 50;
		int updateTime = (int)(1.0/fps*1000000000.0);
		
		long lastUpdateTime = System.nanoTime();

		while(gameRunning){
			long deltaTime = System.nanoTime() - lastUpdateTime;
			
			if(deltaTime > updateTime){
				lastUpdateTime = System.nanoTime();
				update(deltaTime);
				render();
				while(scoreboard == "") {
					gameSetup();
				}
				turnCounter();
				if (nrOfRounds == rounds/2 && nrOfRounds > 0){
					endOfGame();
				}
			}
		}
	}
	/**
	 * Uppdaterar programmet efter varje skott.
	 */
	public void update(long deltaTime) {
		//if(mouseDown.get("clicked")) {
		if (Shot) {

			ballMovement(deltaTime, mousePosX, mousePosY);

			goalkeeperMovement(deltaTime);

			if (ball.getYPos() <= (getMousePosY() - (ballImg.getWidth(null)/2))) {
				ball.setSpeed(0, 0);	

				Shot = false; 
				checkGoal();
				console();

				try {
					Thread.sleep(500);	
				}
				catch (InterruptedException e){}

				reset();
			}
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
	 * 
	 * @param deltaTime skillnaden i tid mellan uppdateringarna
	 * @param mousePosX musens x-positon
	 * @param mousePosY musens y-position
	 */
	public void ballMovement(long deltaTime, int mousePosX, int mousePosY) {

		setVelocityX((mousePosX - (ballPosX+(ballImg.getWidth(null)/2)))/getTime());
		setVelocityY((mousePosY - (ballPosY+(ballImg.getHeight(null)/2)))/getTime());

		ball.setSpeed(getVelocityX(), getVelocityY());

		ball.move(deltaTime);

		ball.setSpeed(0, 0);
	}
	
	/**
	 * Förflyttar målvakten beroende på vilket X 
	 */
	public void goalkeeperMovement(long deltaTime) {
		
		if (getGoalkeeperPos()  == 401) { 
			goalkeeper.setDirectionX(1);
		}
		else if (getGoalkeeperPos() == 240) {
			goalkeeper.setDirectionX(0);
		}
		else if (getGoalkeeperPos() == 79) {
			goalkeeper.setDirectionX(-1);	 
		}

		goalkeeper.setSpeed(400, 0);
		goalkeeper.move(deltaTime);
	}
	
	/**
	 * Sätter upp spelets villkor
	 */
	public void gameSetup() {
		nrOfPlayers = Integer.parseInt(JOptionPane.showInputDialog("1 eller 2 spelare?"));
		nrOfRounds = Integer.parseInt(JOptionPane.showInputDialog("Hur många rundor?"));

		if (nrOfPlayers == 1 ) {
			scoreboard = ScoreboardSingleplayer;
			singleplayer = true;
		}
		else if (nrOfPlayers == 2) {
			scoreboard = ScoreboardMultiplayer;
			multiplayer = true;
		}
		Scoreboard.setText(scoreboard);
	}
	
	/**
	 * Returnerar resultatet av skottet och uppdaterar poängtavlan.
	 */
	public void checkGoal() {	

		if (getMousePosX() >= goalkeeper.getXPos() 
				&& getMousePosX() <= goalkeeper.getXPos() + goalkeeperImg.getWidth(null) 
				&& getMousePosY() >= goalkeeper.getYPos() 
				&& getMousePosY() <= goalkeeper.getYPos() + goalkeeperImg.getHeight(null)) {
			announcer.setText("Räddad");
			setMiss(miss + 1);
		}
		else if (getMousePosY() <= 131 ) {
			announcer.setText("För högt");
			setMiss(miss + 1);
		}	
		else if (getMousePosX() <= 70 || getMousePosX() >= 570) {
			announcer.setText("Utanför");
			setMiss(miss + 1);
		}
		else {
			announcer.setText("Mål!!");
			setGoals(goals + 1);
			if(player1 == true) {
				setPlayer1Point(player1Point + 1);
			}
			else if (player1 == false) {
				setPlayer2Point(player2Point + 1);
			}
		}
		setRounds(rounds +1);

		if (singleplayer) {
			Scoreboard.setText("Missar: "+ getMiss() + " - " + "Mål: "+ getGoals() + " " + " | Runda " + getRounds());
		}
		else if (multiplayer) {
			Scoreboard.setText("Spelare 1: "+ getPlayer1Point() + " - " + "Spelare 2: "+ getPlayer2Point() + " | Runda " + (getRounds()/2 +1));
		}
	}
	
	/**
	 * Loggar resultaten efter varje skott.
	 */
	public void console() {						
		System.out.println("Runda: " + getRounds());							
		System.out.println("Missar: "+ getMiss() + " - "+"Mål: " + getGoals());
		System.out.println("Spelare 1 antal poäng: "+ getPlayer1Point());
		System.out.println("Spelare 2 antal poäng: "+ getPlayer2Point());
		System.out.println(getGoalkeeperPos());
		System.out.println();
	}
	
	/**
	 * Startar om återståller objektens positioner .
	 */
	public void reset() {						
		goalkeeper.setX(233);
		goalkeeper.setY(160);
		ball.setX((gv.getWidth()/2)-ballImg.getWidth(null)/2);
		ball.setY((gv.getHeight()/6) *5);
		//ball.setY(400);
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
	 * Avslutar spelet och avgör vinnaren.
	 */
	public void endOfGame() {

		if(getPlayer1Point() > getPlayer2Point()){
			endResult = JOptionPane.showConfirmDialog(parent, "Spelare 1 vann" + "\n" + "Vill du börja ett nytt spel?", null, JOptionPane.YES_NO_OPTION);
		}
		else if(getPlayer1Point() == getPlayer2Point()) {
			endResult = JOptionPane.showConfirmDialog(parent, "Det blev lika" + "\n" + "Vill du börja ett nytt spel?" , null, JOptionPane.YES_NO_OPTION);
		}
		else {
			endResult = JOptionPane.showConfirmDialog(parent, "Spelare 2 vann" + "\n" + "Vill du börja ett nytt spel?" , null, JOptionPane.YES_NO_OPTION);	
		}
		if (endResult == JOptionPane.YES_OPTION) {
			setPlayer2Point(0);
			setPlayer1Point(0);
			setRounds(0); 
			Scoreboard.setText(scoreboard);
			return;
		}
		else if (endResult == JOptionPane.NO_OPTION) {
			System.exit(0);
		}
		else if(endResult == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
	}
	
	/**
	 * Kollar om mustangenten trycks ner
	 * 
	 * @param MouseEvent e
	 */
	public void mousePressed(MouseEvent e) {
		int mouse = e.getClickCount();
		if(mouse == MouseEvent.MOUSE_PRESSED)
			mouseDown.put("clicked", true);

		Shot = true;

		setGoalkeeperPos(79 + (rand.nextInt(3) * 161));

		setMousePosX(e.getX());
		setMousePosY(e.getY());			
	}
	
	/**
	 * @Override
	 */
	public void mouseReleased(MouseEvent e) {
		int mouse = e.getClickCount();
		if(mouse == MouseEvent.MOUSE_PRESSED)
			mouseDown.put("clicked", false);
		
	}

	/**
	 * @param ballPosX
	 */
	public void setBallPosX(int ballPosX) {
		this.ballPosX = ballPosX;
	}

	/**
	 * @return
	 */
	public int getMousePosX() {
		return mousePosX;
	}
	/**
	 * @param mousePosX
	 */
	public void setMousePosX(int mousePosX) {
		this.mousePosX = mousePosX;
	}

	/**
	 * @return
	 */
	public int getMousePosY() {
		return mousePosY;
	}
	/**
	 * @param mousePosY
	 */
	public void setMousePosY(int mousePosY) {
		this.mousePosY = mousePosY;
	}

	/**
	 * @return
	 */
	public int getGoals() {
		return goals;
	}
	/**
	 * @param goals
	 */
	public void setGoals(int goals) {
		this.goals = goals;
	}

	/**
	 * @return
	 */
	public int getMiss() {
		return miss;
	}
	
	/**
	 * @param miss
	 */
	/**
	 * @param miss
	 */
	public void setMiss(int miss) {
		this.miss = miss;
	}

	/**
	 * @return
	 */
	public int getRounds() {
		return rounds;
	}
	/**
	 * @param rounds
	 */
	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	/**
	 * @param scoreboardSingleplayer
	 */
	public void setScoreboardSingleplayer(String scoreboardSingleplayer) {
		this.ScoreboardSingleplayer = scoreboardSingleplayer;
	}

	/**
	 * @return
	 */
	public double getVelocityX() {
		return velocityX;
	}
	/**
	 * @param velocityX
	 */
	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	/**
	 * @return
	 */
	public double getVelocityY() {
		return velocityY;
	}
	/**
	 * @param velocityY
	 */
	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	/**
	 * @return
	 */
	public double getTime() {
		return timeForShot;
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
	}

	@Override
	public void mouseEntered(MouseEvent e) {	
	}

	@Override
	public void mouseExited(MouseEvent e) {	
	}

	/**
	 * @return
	 */
	public int getGoalkeeperPos() {
		return goalkeeperPos;
	}

	/**
	 * @param gkPos
	 */
	public void setGoalkeeperPos(int gkPos) {
		this.goalkeeperPos = gkPos;
	}

	/**
	 * @return
	 */
	public int getPlayer1Point() {
		return player1Point;
	}

	/**
	 * @param player1Point
	 */
	public void setPlayer1Point(int player1Point) {
		this.player1Point = player1Point;
	}

	/**
	 * @return
	 */
	public int getPlayer2Point() {
		return player2Point;
	}

	/**
	 * @param player2Point
	 */
	public void setPlayer2Point(int player2Point) {
		this.player2Point = player2Point;
	}

	/**
	 * @return
	 */
	public int getAmountOfRounds() {
		return nrOfRounds;
	}

	/**
	 * @param amountOfRounds
	 */
	public void setAmountOfRounds(int amountOfRounds) {
		this.nrOfRounds = amountOfRounds;
	}

	/**
	 * @return
	 */
	public String getScoreboard() {
		return scoreboard;
	}
	/**
	 * @param scoreboard
	 */
	public void setScoreboard(String scoreboard) {
		this.scoreboard = scoreboard;
	}
	
	public int getGoalkeeperPosX() {
		return goalkeeperPosX;
	}
	public void setGoalkeeperPosX(int goalkeeperPosX) {
		this.goalkeeperPosX = goalkeeperPosX;
	}
	
	public double getBallPosY() {
		return ballPosY;
	}
	public void setBallPosY(double ballPosY) {
		this.ballPosY = ballPosY;
	}
	
	public int getGoalkeeperPosY() {
		return goalkeeperPosY;
	}
	public void setGoalkeeperPosY(int goalkeeperPosY) {
		this.goalkeeperPosY = goalkeeperPosY;
	}
}