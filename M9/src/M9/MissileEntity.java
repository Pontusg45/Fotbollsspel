package M9;

import java.awt.Image;

public class MissileEntity extends Entity {

    public MissileEntity(Image image, double xPos, double yPos, int speed) {
        super(image, xPos, yPos, speed);
        dy = -1;
        this.setActive(false);
    }

    @Override
    public void move(long deltaTime) {
        yPos += dy*(deltaTime/1000000000.0)*speed;
    }
}
