package game;

import controller.GameController;
import view.GameView;

/**
 * @auther pontusgustafsson 
 */
public class GameMain {

	public static void main(String[] args) {
		
		GameView gv = new GameView(640, 480,"Fotbollsspel");
		GameController gc = new GameController(gv);
		
		gc.run();
	}
}
