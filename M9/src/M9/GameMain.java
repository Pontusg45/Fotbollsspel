package M9;

public class GameMain {

	   public static void main(String[] args) {
	       GameView gv = new GameView(800, 600,"Game Moment 9");
	       GameController gc = new GameController(gv);
	    
	       gc.run();
	   }
	}

