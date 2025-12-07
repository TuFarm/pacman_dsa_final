package entity;

import java.awt.*;
import main.GamePanel;

public class Pacman extends Entity {

    public char direction = 'R';
    private char nextDirection = 'R';
    public int xVelocity = 0;
    public int yVelocity = 0;
    private int speed = 8; // tileSize / 4

    private Image up, down, left, right;

    public Pacman(GamePanel gp, int x, int y, int width, int height) {
        super(gp, x, y, width, height);

        this.up = gp.pacmanUpImg;
        this.down = gp.pacmanDownImg;
        this.left = gp.pacmanLeftImg;
        this.right = gp.pacmanRightImg;
        this.img = right;
        this.speed = gp.tileSize / 4;
    }

    public void setDirection(char dir) {
        this.nextDirection = dir;
    }

    public void update() {
        // Change the dir if valid (no collision with wall)
        updateVelocity(nextDirection);
        // Check if new dir is valid
        x += xVelocity;
        y += yVelocity;
        boolean colWithNewDir = false;
        for (Entity wall : gp.walls) {
            if (gp.collision(this, wall)) {
                colWithNewDir = true;
                break;
            }
        }
        // Undo move test
        x -= xVelocity;
        y -= yVelocity;

        if (!colWithNewDir) {
            this.direction = nextDirection;
        }

        // move with current dir
        updateVelocity(this.direction);
        this.x += xVelocity;
        this.y += yVelocity;

        // check for collision --> happen --> step back
        for (Entity wall : gp.walls) {
            if (gp.collision(this, wall)) {
                this.x -= xVelocity;
                this.y -= yVelocity;
                break;
            }
        }
        updateImage();
    }

    private void updateVelocity(char dir) {
        switch (dir) {
            case 'U': xVelocity = 0; yVelocity = -speed; break;
            case 'D': xVelocity = 0; yVelocity = speed; break;
            case 'L': xVelocity = -speed; yVelocity = 0; break;
            case 'R': xVelocity = speed; yVelocity = 0; break;
        }
    }

    private void updateImage() {
        switch (direction) {
            case 'U': img = up; break;
            case 'D': img = down; break;
            case 'L': img = left; break;
            case 'R': img = right; break;
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.direction = 'R';
        this.nextDirection = 'R';
        this.img = right;
        this.xVelocity = 0;
        this.yVelocity = 0;
    }
}