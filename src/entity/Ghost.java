package entity;

import java.awt.*;
import java.util.Random;
import main.GamePanel;

public class Ghost extends Entity {

    public char direction = 'U';
    public int xVelocity = 0;
    public int yVelocity = 0;
    private int speed = 8;
    private char[] directions = {'U', 'D', 'L', 'R'};
    private Random random = new Random();

    public Ghost(GamePanel gp, int x, int y, int width, int height, Image img) {
        super(gp, x, y, width, height);
        this.img = img;
        this.speed = gp.tileSize / 4;
        randomDir();
    }

    public void update() {
        this.x += xVelocity;
        this.y += yVelocity;

        if (direction != 'U' && direction != 'D' && y == 32 * 9) {
            updateDir('D');
        }
        for (Entity wall : gp.walls) {
            if (gp.collision(wall, this) || this.x <= 0 || this.x + this.width >= gp.WIDTH) {
                // step back, this will show nothing
                this.x -= xVelocity;
                this.y -= yVelocity;
                // change dir
                randomDir();
            }
        }
    }

    public void randomDir() {
        char newDir = directions[random.nextInt(4)];
        updateDir(newDir);
    }

    public void updateDir(char newDir) {
        this.direction = newDir;
        switch (direction) {
            case 'U': xVelocity = 0; yVelocity = -speed; break;
            case 'D': xVelocity = 0; yVelocity = speed; break;
            case 'L': xVelocity = -speed; yVelocity = 0; break;
            case 'R': xVelocity = speed; yVelocity = 0; break;
        }
    }

    @Override
    public void reset() {
        super.reset();
        randomDir();
    }
}