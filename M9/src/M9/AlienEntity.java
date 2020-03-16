package M9;

import java.awt.Image;

public class AlienEntity extends Entity{

	public AlienEntity(Image image, int xPos, int yPos, int speed) {
		super(image, xPos, yPos, speed);
	}

	@Override
	public void move(long deltaTime) {
		yPos += dx*(deltaTime/1000000000.0)*speed;	
	}

	public double getyPosX() {
		// TODO Auto-generated method stub
		return 0;
	}
}
